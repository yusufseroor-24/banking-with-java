package com.ga.project1.banking;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Login {
    private CustomerData customerData;

    private static final int MAX_ATTEMPTS = 3;
    private static final long LOCK_DURATION = 60;

    private static final Map<String, Integer> failedAttempts = new HashMap<>();
    private static final Map<String, LocalDateTime> lockedUntil = new HashMap<>();

    public Login(CustomerData customerData) {this.customerData = customerData;}

    public Customer login(){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter Username: ");
        String username = scanner.nextLine();

        if (isLocked(username)) {
            long secondsLeft = ChronoUnit.SECONDS.between(LocalDateTime.now(), lockedUntil.get(username));
            System.out.println("This account is locked due to too many failed attempts. Try again in " + secondsLeft + " seconds.");
            return null;
        }

        System.out.println("Enter Password: ");
        String password = scanner.nextLine();

        Customer customer = customerData.findUserByUsername(username);

        if(customer != null){
            boolean isValid = PasswordUtil.verifyPassword(password, customer.getSalt(), customer.getPasswordHash());
            if(isValid){
                Bank.loggedIn=true;
                failedAttempts.remove(username);
                System.out.println("--------- Welcome to ACME Bank ---------");
                return customer;
            }
        }
        recordFailedAttempt(username);
        System.out.println("invalid username or password. please try again or check credentials.");
        return null;
    }

    private boolean isLocked(String username){
        LocalDateTime until = lockedUntil.get(username);
        if(until == null)
            return false;
        if (LocalDateTime.now().isBefore(until)) {
            return true;
        } else {
            lockedUntil.remove(username);
            failedAttempts.remove(username);
            return false;
        }
    }

    private void recordFailedAttempt(String username) {
        int attempts = failedAttempts.getOrDefault(username, Integer.valueOf(0)) + 1;
        failedAttempts.put(username, Integer.valueOf(attempts));

        if (attempts >= MAX_ATTEMPTS) {
            lockedUntil.put(username, LocalDateTime.now().plusSeconds(LOCK_DURATION));
            failedAttempts.remove(username);
            System.out.println("Too many failed attempts. This account is now locked for 1 minute.");
        } else {
            System.out.println("Attempts remaining before lockout: " + (MAX_ATTEMPTS - attempts));
        }
    }
}
