package com.ga.project1.banking;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CustomerData {
    private static final String FILE_NAME = "customer.txt";

    public void saveCustomer(Customer customer) {
        try (FileWriter writer = new FileWriter(FILE_NAME, true)) {
            writer.write(
                    customer.getCustomerID() + "," +
                            customer.getName() + "," +
                            customer.getUsername() + "," +
                            customer.getPasswordHash() + "," +
                            customer.getSalt() + "," +
                            customer.getRole() +
                            System.lineSeparator()
            );
        } catch (IOException e) {
            System.out.println("Error saving customer.");
            throw new RuntimeException(e);
        }
    }

    //find customer by username
    public Customer findUserByUsername(String username) {
        try (BufferedReader reader =
                     new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6 && data[2].equals(username)) {
                    return new Customer(
                            data[4],                   // salt
                            Integer.parseInt(data[0]), // customerID
                            data[1],                   // name
                            data[2],                   // username
                            data[3],                   // passwordHash
                            data[5]                    // role
                    );
                }
            }

        } catch (FileNotFoundException e) {
            return null;
        } catch (IOException e) {
            System.out.println("Error reading customer file.");
            throw new RuntimeException(e);
        }
        return null;
    }

    // Generate the next customer ID
    public int getNextCustomerID(){
        int highestID = 1000;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 6) {
                    int customerID = Integer.parseInt(data[0]);
                    if (customerID > highestID) {
                        highestID = customerID;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            return 1001;

        } catch (IOException e) {
            System.out.println("Error reading customer data.");
        }
        return highestID + 1;
    }

    static void checkBankerExists(CustomerData customerData){
        if(customerData.findUserByUsername("banker1") == null){
            Customer banker1 = new Customer("banker1", "banker1", 1, "BankerOne", "banker");
            customerData.saveCustomer(banker1);
        }
    }
}


