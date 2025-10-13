package org.example;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JOptionPane;

public class Message {
    private String messageID;
    private int numMessagesSent;
    private String recipient;
    private String message;
    private String messageHash;
    private static int totalMessages = 0;
    private static List<Message> sentMessages = new ArrayList<>();
    private static List<Message> storedMessages = new ArrayList<>();

    public Message(String recipient, String message) {
        this.messageID = generateMessageID();
        this.numMessagesSent = ++totalMessages;
        this.recipient = recipient;
        this.message = message;
        this.messageHash = createMessageHash();
    }

    // Method to check if message ID is not more than 10 characters
    public boolean checkMessageID() {
        return messageID != null && messageID.length() <= 10;
    }

    // Method to check, recipient cell number format (reusing from login class)
    public boolean checkRecipientCell() {
        return recipient.matches("^\\+27\\d{9}$");
    }

    // Method to create message hash
    public String createMessageHash() {
        String firstTwoID = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        String[] words = message.split(" ");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;

        return firstTwoID + ":" + numMessagesSent + ":" +
                (firstWord + lastWord).toUpperCase();
    }

    // Method to handle, sending, storing, or disregarding, message
    public String sentMessage(int choice) {
        switch (choice) {
            case 1: // Send Message
                sentMessages.add(this);
                displayMessageDetails();
                return "Message successfully sent.";

            case 2: // Disregard Message
                totalMessages--; // Decrement since we're not counting this
                return "Press 0 to delete message.";

            case 3: // Store Message
                storedMessages.add(this);
                return "Message successfully stored.";

            default:
                return "Invalid choice.";
        }
    }

    // Method to display message details using JOptionPane
    private void displayMessageDetails() {
        String details = "MessageID: " + messageID + "\n" +
                "Message Hash: " + messageHash + "\n" +
                "Recipient: " + recipient + "\n" +
                "Message: " + message;
        JOptionPane.showMessageDialog(null, details, "Message Details", JOptionPane.INFORMATION_MESSAGE);
    }

    // Method to return all sent messages
    public static String printMessages() {
        if (sentMessages.isEmpty()) {
            return "No messages sent.";
        }

        StringBuilder sb = new StringBuilder();
        for (Message msg : sentMessages) {
            sb.append("MessageID: ").append(msg.messageID)
                    .append(", Hash: ").append(msg.messageHash)
                    .append(", Recipient: ").append(msg.recipient)
                    .append(", Message: ").append(msg.message)
                    .append("\n");
        }
        return sb.toString();
    }

    // Method to return total number of messages sent
    public static int returnTotalMessages() {
        return sentMessages.size();
    }

    // Method to check if the message exceeds 250 characters
    public boolean checkMessageLength() {
        return message.length() <= 250;
    }

    // Method to get the message length validation message
    public String getMessageLengthMessage() {
        if (checkMessageLength()) {
            return "Message ready to send.";
        } else {
            int excess = message.length() - 250;
            return "Message exceeds 250 characters by " + excess + ", please reduce size.";
        }
    }

    // Method to get the recipient validation message
    public String getRecipientValidationMessage() {
        if (checkRecipientCell()) {
            return "Cell phone number successfully captured.";
        } else {
            return "Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.";
        }
    }

    // Helper method to generate random 10-digit message ID
    private String generateMessageID() {
        Random rand = new Random();
        long id = 1000000000L + (long)(rand.nextDouble() * 9000000000L);
        return String.valueOf(id);
    }

}