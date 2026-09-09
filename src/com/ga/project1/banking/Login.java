package com.ga.project1.banking;
import java.util.Scanner;
import static com.ga.project1.banking.Bank.menu2;

public class Login {
    private CustomerData customerData;

    public Login(CustomerData customerData) {
        this.customerData = customerData;
    }

    public Customer login(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Username: ");
        String username = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        Customer customer = customerData.findUserByUsername(username);

        if(customer != null){
            boolean isValid = PasswordUtil.verifyPassword(password, customer.getSalt(), customer.getPasswordHash());
            if(isValid){
                Bank.loggedIn=true;
                System.out.println("--------- Welcome to ACME Bank ---------");
                return customer;
            }
        }
        System.out.println("invalid username or password. please try again or check credentials.");
        return null;
    }
}
