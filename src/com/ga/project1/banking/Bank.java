package com.ga.project1.banking;

import java.util.Scanner;

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
                        menu2(customer, accountData);
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
                    Account checkingAccount = new Account(customerID, 0, "checking", checkingNumber);
                    accountData.saveAccount(checkingAccount);
                    System.out.println("Account created successfully!");
                    System.out.println("Would you like to create a saving account? (Y/N)");
                    String userans = scanner.nextLine();
                    if (userans.equalsIgnoreCase("Y")) {
                        String savingNumber = accountData.generateAccountNumber();
                        Account savingAccount = new Account(customerID, 0, "saving", savingNumber);
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
            System.out.println("3. Logout");

            int userChoice = scanner.nextInt();
            switch (userChoice) {
                case 1:
                    accountType = "checking";
                    Account selectedAccount = accountData.findCustomerAccount(customer.getCustomerID(), accountType);
                    customerMenu(selectedAccount, accountData);
                    break;
                case 2:
                    accountType = "saving";
                    Account selectedAccountSaving = accountData.findCustomerAccount(customer.getCustomerID(), accountType);
                    customerMenu(selectedAccountSaving, accountData);
                    break;
                case 3:
                    customerMenuRunning = false;
                    loggedIn = false;
                    System.out.println("You have logged out");
            }
        }
    }

        public static void customerMenu(Account selectedAccount, AccountData accountData){
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
            System.out.println("5. Logout");

            int answer = scanner.nextInt();
            switch (answer) {
                case 1:
                    System.out.println("How much would you like to withdraw?");
                    double withrdawAmount = scanner.nextDouble();
                    withdrawMoney(selectedAccount, withrdawAmount, accountData);
                    break;
                case 2:
                    System.out.println("How much would you like to deposit?");
                    double depositAmount = scanner.nextDouble();
                    depositMoney(selectedAccount, depositAmount, accountData);
                    break;
                case 3:
                    System.out.println("How much would you like to transfer money?");
                    double transferAmount = scanner.nextDouble();
                    scanner.nextLine();
                    System.out.println("To which account would you like the money to be transferred to?");
                    String toAccountNumber = scanner.nextLine();
                    transferMoney(selectedAccount, toAccountNumber, transferAmount, accountData);
                    break;
                case 4:
                    System.out.println("Your balance is: " + selectedAccount.getBalance() + "BD");
                    break;
                case 5:
                    loggedIn = false;
                    System.out.println("You have been logged out.");
                    break;
                default:
                    System.out.println("Invalid Option. Please choose an option from the provided menu.");
            }

            }

        }
}
