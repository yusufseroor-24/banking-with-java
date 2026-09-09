package com.ga.project1.banking;

import java.io.*;

public class AccountData {
    private static final String FILE_NAME = "account.txt";

    //Save an account
    public void saveAccount(Account account) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(
                    account.getAccountNumber() + "," +
                            account.getCustomerID() + "," +
                            account.getAccountType() + "," +
                            account.getBalance() +
                            System.lineSeparator()
            );
        } catch (IOException e) {
            System.out.println("Error saving account.");
            e.printStackTrace();
        }
    }

    // Find account using account number
    public Account findByAccountNumber(String accountNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;

            while ((line = reader.readLine()) != null) {

                String[] data = line.split(",");

                if (data.length == 4 &&
                        data[0].equals(accountNumber)) {

                    return new Account(
                            Integer.parseInt(data[1]),
                            Integer.parseInt(data[3]),
                            data[2],
                            data[0]
                    );
                }
            }

        } catch (FileNotFoundException e) {
            return null;

        } catch (IOException e) {
            System.out.println("Error reading account data.");
        }

        return null;
    }
    //Find an account belonging to a specific customer
    public Account findCustomerAccount(int customerID, String accountType) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");

                if (data.length == 4) {
                    int storedCustomerID = Integer.parseInt(data[1]);
                    if (storedCustomerID == customerID &&
                            data[2].equalsIgnoreCase(accountType)) {

                        return new Account(Integer.parseInt(data[1]), Integer.parseInt(data[3]), data[2], data[0]);
                    }
                }
            }

        } catch (FileNotFoundException e) {
            return null;

        } catch (IOException e) {
            System.out.println("Error reading account data.");
        }
        return null;
    }

    //Update an account's balance
    public void updateAccount(Account updatedAccount) {

        File inputFile = new File(FILE_NAME);
        File tempFile = new File("account_temp.txt");

        try (
                BufferedReader reader = new BufferedReader(new FileReader(inputFile));
                FileWriter writer = new FileWriter(tempFile)
        ) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 4 &&
                        data[0].equals(updatedAccount.getAccountNumber())) {
                    writer.write(
                            updatedAccount.getAccountNumber() + "," +
                                    updatedAccount.getCustomerID() + "," +
                                    updatedAccount.getAccountType() + "," +
                                    updatedAccount.getBalance() +
                                    System.lineSeparator()
                    );
                } else {
                    writer.write(line + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            System.out.println("Error updating account.");
            return;
        }

        if (!inputFile.delete()) {
            System.out.println("Could not delete old account file.");
            return;
        }

        if (!tempFile.renameTo(inputFile)) {
            System.out.println("Could not update account file.");
        }
    }

    //Generate a random 12-digit account number
    public String generateAccountNumber() {
        long number = 100000000000L + (long) (Math.random() * 900000000000L);
        return String.valueOf(number);
    }
}