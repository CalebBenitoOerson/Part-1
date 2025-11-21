package org.example;

import java.util.Scanner;

public class main {
    private static MessageManager messageManager;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        messageManager = new MessageManager();

        // [Your existing Part 1 and Part 2 code remains unchanged]
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

        if (!regMessage.equals("User registered successfully.")) {
            System.out.println("Registration failed. Exiting...");
            scanner.close();
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

        if (!success) {
            System.out.println("Login failed. Exiting...");
            scanner.close();
            return;
        }

        // Enhanced Messaging System Menu
        System.out.println("Welcome to QuickChat.");

        boolean running = true;
        while (running) {
            System.out.println("\nPlease choose an option:");
            System.out.println("1) Send Messages");
            System.out.println("2) Show recently sent messages");
            System.out.println("3) Message Management (Part 3 Features)");
            System.out.println("4) Quit");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    sendMessages(scanner);
                    break;
                case 2:
                    System.out.println("Coming Soon.");
                    break;
                case 3:
                    showPart3Features(scanner);
                    break;
                case 4:
                    running = false;
                    System.out.println("Thank you for using QuickChat. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }

        scanner.close();
    }

    private static void sendMessages(Scanner scanner) {
        System.out.println("How many messages do you wish to enter?");
        int numMessages = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        for (int i = 0; i < numMessages; i++) {
            System.out.println("\nMessage " + (i + 1) + ":");
            System.out.println("Enter recipient cell phone number (+27 format):");
            String recipient = scanner.nextLine();

            System.out.println("Enter your message (max 250 characters):");
            String message = scanner.nextLine();

            Message msg = new Message(recipient, message);

            // Validate message length
            if (!msg.checkMessageLength()) {
                System.out.println(msg.getMessageLengthMessage());
                i--; // Retry this message
                continue;
            }

            // Validate recipient
            if (!msg.checkRecipientCell()) {
                System.out.println(msg.getRecipientValidationMessage());
                i--; // Retry this message
                continue;
            }

            // Ask user what to do with the message
            System.out.println("Choose an option for this message:");
            System.out.println("1) Send Message");
            System.out.println("2) Disregard Message");
            System.out.println("3) Store Message to send later");

            int messageChoice = scanner.nextInt();
            scanner.nextLine();

            String result = msg.sentMessage(messageChoice);
            System.out.println(result);
        }

        // Display total messages sent
        System.out.println("\nTotal messages sent: " + Message.returnTotalMessages());

        // Display all sent messages
        System.out.println("\nAll sent messages:");
        System.out.println(Message.printMessages());
    }


    private static void showPart3Features(Scanner scanner) {
        boolean inPart3Menu = true;

        while (inPart3Menu) {
            System.out.println("\n=== PART 3: MESSAGE MANAGEMENT ===");
            System.out.println("1) Display sent messages senders and recipients");
            System.out.println("2) Display longest sent message");
            System.out.println("3) Search messages by recipient");
            System.out.println("4) Delete message by hash");
            System.out.println("5) Display full message report");
            System.out.println("6) Read messages from JSON file");
            System.out.println("7) Back to main menu");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    System.out.println(messageManager.displaySentMessagesSenders());
                    break;
                case 2:
                    System.out.println(messageManager.displayLongestMessage());
                    break;
                case 3:
                    System.out.println("Enter recipient to search:");
                    String recipient = scanner.nextLine();
                    System.out.println(messageManager.searchMessagesByRecipient(recipient));
                    break;
                case 4:
                    System.out.println("Enter message hash to delete:");
                    String hash = scanner.nextLine();
                    System.out.println(messageManager.deleteMessageByHash(hash));
                    break;
                case 5:
                    System.out.println(messageManager.displayMessageReport());
                    break;
                case 6:
                    System.out.println("Enter JSON filename:");
                    String filename = scanner.nextLine();
                    System.out.println(messageManager.readMessagesFromJSON(filename));
                    break;
                case 7:
                    inPart3Menu = false;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }
}