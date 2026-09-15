package com.ga.project1.banking;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AccountData {
    private static final String FILE_NAME = "account.txt";

    public void saveAccount(Account account) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(
                    account.getAccountNumber() + "," +
                            account.getCustomerID() + "," +
                            account.getAccountType() + "," +
                            account.getBalance() + "," +
                            account.isActive() + "," +
                            account.getOverDraftCount() + "," +
                            account.getCardType() + "," +
                            account.getWithdrawnToday() + "," +
                            account.getTransferredToday() + "," +
                            account.getTransferredOwnToday() + "," +
                            account.getDepositedToday() + "," +
                            account.getDepositedOwnToday() + "," +
                            account.getLastResetDate() + "," +
                            account.getStatus() +
                            System.lineSeparator()
            );
        } catch (IOException e) {
            System.out.println("Error saving account.");
            e.printStackTrace();
        }
    }

    //Find account using account number
    public Account findByAccountNumber(String accountNumber) {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 14 && data[0].equals(accountNumber)) {
                    return buildAccount(data);
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

                if (data.length == 14) {
                    int storedCustomerID = Integer.parseInt(data[1]);
                    if (storedCustomerID == customerID &&
                            data[2].equalsIgnoreCase(accountType)) {

                        return buildAccount(data);
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

    private Account buildAccount(String[] data) {
        return new Account(
                Integer.parseInt(data[1]),        //customerID
                Double.parseDouble(data[3]),      //balance
                data[2],                          //accountType
                data[0],                          //accountNumber
                Boolean.parseBoolean(data[4]),    //isActive
                Integer.parseInt(data[5]),        //overDraftCount
                CardType.valueOf(data[6]),        //cardType
                Double.parseDouble(data[7]),      //withdrawnToday
                Double.parseDouble(data[8]),      //transferredToday
                Double.parseDouble(data[9]),      //transferredOwnToday
                Double.parseDouble(data[10]),     //depositedToday
                Double.parseDouble(data[11]),     //depositedOwnToday
                LocalDate.parse(data[12]),        //lastResetDate
                data[13]                          //status
        );
    }

    //Update an account
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
                if (data.length == 14 && data[0].equals(updatedAccount.getAccountNumber())) {
                    writer.write(
                            updatedAccount.getAccountNumber() + "," +
                                    updatedAccount.getCustomerID() + "," +
                                    updatedAccount.getAccountType() + "," +
                                    updatedAccount.getBalance() + "," +
                                    updatedAccount.isActive() + "," +
                                    updatedAccount.getOverDraftCount() + "," +
                                    updatedAccount.getCardType() + "," +
                                    updatedAccount.getWithdrawnToday() + "," +
                                    updatedAccount.getTransferredToday() + "," +
                                    updatedAccount.getTransferredOwnToday() + "," +
                                    updatedAccount.getDepositedToday() + "," +
                                    updatedAccount.getDepositedOwnToday() + "," +
                                    updatedAccount.getLastResetDate() + "," +
                                    updatedAccount.getStatus() +
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
    public String generateAccountNumber(){
        long number = 100000000000L + (long) (Math.random() * 900000000000L);
        return String.valueOf(number);
    }

    public List<Account> findAllPendingAccounts(){
        try (Stream<String> lines = Files.lines(Path.of(FILE_NAME))) {
            return lines
                    .map(line -> line.split(","))
                    .filter(data -> data.length == 14 && data[13].equals("PENDING"))
                    .map(this::buildAccount)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }


}