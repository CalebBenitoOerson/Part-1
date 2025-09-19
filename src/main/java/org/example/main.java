package org.example;

import java.util.Scanner;

public class main {
    public static void Main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Registration
        System.out.println("Enter First Name: ");
        String firstName = scanner.nextLine();
        System.out.println("Enter Last Name: ");
        String lastName = scanner.nextLine();
        System.out.println("Enter Username: ");
        String username = scanner.nextLine();
        System.out.println("Enter Password: ");
        String password = scanner.nextLine();
        System.out.println("Enter Cell Phone (+27 format): ");
        String cellPhone = scanner.nextLine();

        login user = new login(firstName, lastName, username, password, cellPhone);

        // Registration messages
        String regMessage = user.registerUser();
        System.out.println(regMessage);

        if (regMessage.equals("User registered successfully.")) {
            System.out.println("Registration failed. Exiting...");
            return;
        }

        // Login
        System.out.println("LOGIN");
        System.out.println("Enter Username: ");
        String loginUser = scanner.nextLine();
        System.out.println("Enter Password: ");
        String loginPass = scanner.nextLine();

        boolean success = user.loginUser(loginUser, loginPass);
        System.out.println(user.returnLoginStatus(success));

        scanner.close();
    }
}