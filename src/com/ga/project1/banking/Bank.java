package com.ga.project1.banking;

import java.util.Scanner;

public class Bank {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        while(running){
            System.out.println("=========ACME Bank Menu=========");
            System.out.println("1. Login");
            System.out.println("2. Create an Account");
            System.out.println("3. Exit");

            int userChoice = scanner.nextInt();

            switch (userChoice){
                case 1:
                    Customer customer = new Customer(1, "Yusuf", "yusuf123", "hashpass","Salt", 500, "customer");
                    Login login = new Login();
                    login.login(customer);
                    break;
                case 2:
                    System.out.println();
                    break;
                case 3:
                    System.out.println();
                    running = false;
                    break;
                default:
                    System.out.println("Invalid Option. Please choose an available option from the menu.");
            }
        }
        scanner.close();
    }
}
