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
    public int getNextCustomerID() {
        int highestID = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 5) {
                    int customerID = Integer.parseInt(data[0]);
                    if (customerID > highestID) {
                        highestID = customerID;
                    }
                }
            }
        } catch (FileNotFoundException e) {
            return 1;

        } catch (IOException e) {
            System.out.println("Error reading customer data.");
        }
        return highestID + 1;
    }

        //get all customers
//    public List<Customer> getAllCustomers(){
//        List<Customer> customers = new ArrayList<>();
//        try(BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))){
//            String line;
//            while ((line = reader.readLine()) != null) {
//                String[] data = line.split(",");
//
//                if (data.length == 5) {
//                    Customer customer = new Customer(data[0], data[1], data[2], Integer.parseInt(data[3]),data[4], data[5]);
//                    customers.add(customer);
//                }
//            }
//        } catch (IOException e) {
//            System.out.println("Error reading customers.");
//        }
//        return customers;
//    }
}


