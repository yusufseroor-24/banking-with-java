package com.ga.project1.banking;
import java.util.Scanner;

public class Login {
    public boolean login(Customer customer){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Username: ");
        String username = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        if(username.equals(customer.getUsername())){
            boolean isValid = PasswordUtil.verifyPassword(password, customer.getSalt(), customer.getPasswordHash());
            if(isValid){
                System.out.println("welcome to ACME Bank.");
                return true;
            }
        }
        System.out.println("invalid username or password. please try or check credentials.");
        return false;
    }
}
