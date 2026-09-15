package com.ga.project1.banking;

import java.util.List;
import java.util.Scanner;

import static com.ga.project1.banking.Account.upgradeCard;
import static com.ga.project1.banking.CustomerData.checkBankerExists;
import static com.ga.project1.banking.Transactions.*;

public class Bank {
    public static boolean loggedIn = false;
    private static String accountType;

    public static void main(String[] args) {
        menu1();
    }

    static Scanner scanner = new Scanner(System.in);

    public static void menu1() {
        boolean running = true;
        CustomerData customerData = new CustomerData();
        AccountData accountData = new AccountData();
        Login login = new Login(customerData);

        checkBankerExists(customerData);

        while (running) {
            System.out.println("=========ACME Bank Menu=========");
            System.out.println("1. Login");
            System.out.println("2. Create an Account");
            System.out.println("3. Exit");

            int userChoice = scanner.nextInt();
            scanner.nextLine();

            switch (userChoice) {
                case 1:
                    Customer customer = login.login();

                    if (customer != null) {
                        loggedIn = true;
                        if(customer.getRole().equalsIgnoreCase("banker")){
                            bankerMenu(customer, accountData);
                        }else {
                            menu2(customer, accountData);
                        }
                    }
                    break;
                case 2:
                    System.out.println("\nCreate Account");

                    System.out.println("Enter your name: ");
                    String name = scanner.nextLine();

                    System.out.println("Enter Username: ");
                    String username = scanner.nextLine();

                    System.out.println("Enter Password: ");
                    String password = scanner.nextLine();

                    int customerID = customerData.getNextCustomerID();

                    Customer newCustomer = new Customer(
                            password,
                            username,
                            customerID,
                            name,
                            "customer"
                    );
                    customerData.saveCustomer(newCustomer);

                    //create automatic checking account
                    String checkingNumber = accountData.generateAccountNumber();
                    Account checkingAccount = new Account(customerID, 0, "checking", checkingNumber, true, 0, CardType.STANDARD_MASTERCARD);
                    accountData.saveAccount(checkingAccount);
                    System.out.println("Account created successfully! It's pending, banker approval needed before you can use it.");
                    System.out.println("Would you like to create a saving account? (Y/N)");
                    String userans = scanner.nextLine();
                    if (userans.equalsIgnoreCase("Y")) {
                        String savingNumber = accountData.generateAccountNumber();
                        Account savingAccount = new Account(customerID, 0, "saving", savingNumber, true, 0, CardType.STANDARD_MASTERCARD);
                        accountData.saveAccount(savingAccount);
                        System.out.println("Saving account created successfully");
                        System.out.println("Saving Account Number: " + savingNumber);
                    } else {
                        System.out.println("Creation Failed.");
                    }
                    break;
                case 3:
                    System.out.println("Thank you for using ACME bank");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid Option. Please choose an available option from the menu.");
            }
        }
        scanner.close();
    }

    public static void menu2(Customer customer, AccountData accountData) {
        boolean customerMenuRunning = true;

        while (loggedIn) {
            System.out.println("============= Customer Menu =============");
            System.out.println("Welcome, " + customer.getName());
            System.out.println("\nSelect an account:");
            System.out.println("1. Checking Account");
            System.out.println("2. Saving Account");
            System.out.println("3. Transaction History");
            System.out.println("4. Currency Conversions");
            System.out.println("5. Logout");

            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1:
                    accountType = "checking";
                    Account selectedAccount = accountData.findCustomerAccount(customer.getCustomerID(), accountType);
                    if (selectedAccount == null) {
                        System.out.println("No checking account found for this customer.");
                        break;
                    }
                    if (!selectedAccount.getStatus().equals("APPROVED")) {
                        System.out.println("This account is still pending banker approval.");
                        break;
                    }
                    customerMenu(selectedAccount, accountData, customer);
                    break;
                case 2:
                    accountType = "saving";
                    Account selectedAccountSaving = accountData.findCustomerAccount(customer.getCustomerID(), accountType);
                    if (selectedAccountSaving == null) {
                        System.out.println("No saving account found for this customer.");
                        break;
                    }
                    if (!selectedAccountSaving.getStatus().equals("APPROVED")) {
                        System.out.println("This account is still pending banker approval.");
                        break;
                    }
                    customerMenu(selectedAccountSaving, accountData, customer);
                    break;
                case 3:
                    customerTransactionHistory(customer);
                    break;
                case 4:
                    System.out.println("Choose the currency to convert to Bahraini Dinar (BD)");
                    System.out.println("1. US Dollar ($)");
                    System.out.println("2. British Pound (£)");
                    System.out.println("3. Euro (€)");
                    System.out.println("4. Saudi Riyal (SAR)");
                    int conversionUserChoice = scanner.nextInt();
                    currencyConversion(conversionUserChoice);
                    break;
                case 5:
                    customerMenuRunning = false;
                    loggedIn = false;
                    System.out.println("You have logged out");
            }
        }
    }

        public static void customerMenu(Account selectedAccount, AccountData accountData, Customer customer){
            boolean running = true;

            while(running && loggedIn){
            System.out.println("\n====== " + selectedAccount.getAccountType() + " account ======");
            System.out.println("\n====== " + selectedAccount.getAccountNumber() + " ======");
            System.out.println("\n====== " + selectedAccount.getBalance() + " BD ======");
            System.out.println("\nSelect the service needed:");
            System.out.println("1. Withdraw Money");
            System.out.println("2. Deposit Money");
            System.out.println("3. Transfer Money");
            System.out.println("4. Check Balance");
            System.out.println("5. Filter Transactions");
            System.out.println("6. Manage Card");
            System.out.println("7. Return");
            System.out.println("8. Logout");

            int answer = scanner.nextInt();
            scanner.nextLine();
            switch (answer) {
                case 1:
                    System.out.println("How much would you like to withdraw?");
                    double withrdawAmount = scanner.nextDouble();
                    withdrawMoney(selectedAccount, withrdawAmount, accountData, customer);
                    break;
                case 2:
                    System.out.println("1. Deposit into this account");
                    System.out.println("2. Deposit into another account");
                    int depositChoice = scanner.nextInt();
                    scanner.nextLine();

                    if (depositChoice == 1) {
                        System.out.println("How much would you like to deposit?");
                        double depositAmount = scanner.nextDouble();
                        depositMoney(selectedAccount, depositAmount, accountData, customer, true);

                    } else if (depositChoice == 2) {
                        System.out.println("Enter the account number to deposit into:");
                        String targetAccountNumber = scanner.nextLine();
                        Account targetAccount = accountData.findByAccountNumber(targetAccountNumber);
                        if (targetAccount == null) {
                            System.out.println("Account not found.");
                            break;
                        }
                        System.out.println("How much would you like to deposit?");
                        double depositAmount = scanner.nextDouble();
                        depositMoney(targetAccount, depositAmount, accountData, customer, false);

                    } else {
                        System.out.println("Invalid option.");
                    }
                    break;
                case 3:
                    System.out.println("How much would you like to transfer money?");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("To which account would you like the money to be transferred to?");
                    String toAccountNumber = scanner.nextLine();
                    transferMoney(selectedAccount, toAccountNumber, transferAmount, accountData, customer);
                    break;
                case 4:
                    System.out.println("Your balance is: " + selectedAccount.getBalance() + "BD");
                    break;
                case 5:
                    System.out.println("Filter by:");
                    System.out.println("1. Today");
                    System.out.println("2. Yesterday");
                    System.out.println("3. Last 7 Days");
                    System.out.println("4. Last 30 Days");
                    System.out.println("5. Last Week");
                    System.out.println("6. Last Month");
                    int filterUserChoice = scanner.nextInt();
                    filterTransaction(selectedAccount, customer, filterUserChoice);
                    break;
                case 6:
                    System.out.println("Which card type would you like to upgrade to? TITANIUM or PLATINUM");
                    String upgradeChoice = scanner.nextLine();
                    upgradeCard(selectedAccount, upgradeChoice);
                    accountData.updateAccount(selectedAccount);
                    break;
                case 7:
                    running=false;
                    break;
                case 8:
                    loggedIn = false;
                    System.out.println("You have been logged out.");
                    break;
                default:
                    System.out.println("Invalid Option. Please choose an option from the provided menu.");
            }
            }
        }

    public static void bankerMenu(Customer banker, AccountData accountData) {
        boolean running = true;

        while (running && loggedIn) {
            System.out.println("============= Banker Menu =============");
            System.out.println("Welcome, " + banker.getName());
            System.out.println("1. Review Pending Accounts");
            System.out.println("2. Logout");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    List<Account> pending = accountData.findAllPendingAccounts();
                    if(pending.isEmpty()) {
                        System.out.println("No accounts are currently pending approval.");
                        break;
                    }
                    for (Account account : pending) {
                        System.out.println("Account: " + account.getAccountNumber()
                                + " | Customer ID: " + account.getCustomerID()
                                + " | Type: " + account.getAccountType());
                    }
                    System.out.println("Enter an account number to approve (or 0 to go back):");
                    String targetNumber = scanner.nextLine();
                    if (!targetNumber.equals("0")) {
                        Account target = accountData.findByAccountNumber(targetNumber);
                        if (target != null && target.getStatus().equals("PENDING")) {
                            target.setStatus("APPROVED");
                            accountData.updateAccount(target);
                            System.out.println("Account " + targetNumber + " approved.");
                        } else {
                            System.out.println("Account not found or not pending.");
                        }
                    }
                    break;
                case 2:
                    loggedIn = false;
                    System.out.println("Banker logged out.");
                    break;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}
