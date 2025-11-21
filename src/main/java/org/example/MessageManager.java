package org.example;

import java.util.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;

public class MessageManager {
    // Arrays as specified in Part 3 requirements
    private List<String> sentMessages;
    private List<String> disregardedMessages;
    private List<String> storedMessages;
    private List<String> messageHashes;
    private List<String> messageIDs;

    private Gson gson;

    public MessageManager() {
        this.sentMessages = new ArrayList<>();
        this.disregardedMessages = new ArrayList<>();
        this.storedMessages = new ArrayList<>();
        this.messageHashes = new ArrayList<>();
        this.messageIDs = new ArrayList<>();
        this.gson = new Gson();

        // Initialize with test data from Part 3 requirements
        initializeTestData();
    }

    private void initializeTestData() {
        // Clear any existing data
        sentMessages.clear();
        disregardedMessages.clear();
        storedMessages.clear();
        messageHashes.clear();
        messageIDs.clear();

        // Test Data Message 1 - SENT
        String message1 = "Did you get the cake?";
        addMessageToArray(sentMessages, "+27834557896", message1, "Sent");

        // Test Data Message 2 - STORED (this is the longest message)
        String message2 = "Where are you? You are late! I have asked you to be on time.";
        addMessageToArray(storedMessages, "+27838884567", message2, "Stored");

        // Test Data Message 3 - DISREGARDED
        String message3 = "Yohoooo, I am at your gate.";
        addMessageToArray(disregardedMessages, "+27834484567", message3, "Disregard");

        // Test Data Message 4 - SENT
        String message4 = "It is dinner time!";
        addMessageToArray(sentMessages, "0838884567", message4, "Sent");

        // Test Data Message 5 - STORED
        String message5 = "Ok, I am leaving without you.";
        addMessageToArray(storedMessages, "+27838884567", message5, "Stored");
    }

    private void addMessageToArray(List<String> targetArray, String recipient, String message, String flag) {
        String messageEntry = recipient + "|" + message + "|" + flag;
        targetArray.add(messageEntry);

        // Generate and store message ID and hash
        String messageID = generateMessageID();
        String messageHash = generateMessageHash(messageID, message);

        messageIDs.add(messageID);
        messageHashes.add(messageHash);
    }

    // 2a. Display sender and recipient of all sent messages
    public String displaySentMessagesSenders() {
        if (sentMessages.isEmpty()) {
            return "No sent messages found.";
        }

        StringBuilder result = new StringBuilder();
        result.append("Sent Messages - Senders and Recipients:\n");
        for (String message : sentMessages) {
            String[] parts = message.split("\\|");
            if (parts.length >= 2) {
                result.append("Recipient: ").append(parts[0])
                        .append(" | Message: ").append(parts[1].substring(0, Math.min(20, parts[1].length())))
                        .append("...\n");
            }
        }
        return result.toString();
    }

    // 2b. Display the longest sent message - FIXED METHOD
    public String displayLongestMessage() {
        if (sentMessages.isEmpty()) {
            return "No sent messages found.";
        }

        String longestMessage = "";
        int maxLength = 0;

        for (String messageEntry : sentMessages) {
            String[] parts = messageEntry.split("\\|");
            if (parts.length >= 2) {
                String messageText = parts[1];
                if (messageText.length() > maxLength) {
                    maxLength = messageText.length();
                    longestMessage = messageText;
                }
            }
        }

        // Also check stored messages for the longest message overall
        for (String messageEntry : storedMessages) {
            String[] parts = messageEntry.split("\\|");
            if (parts.length >= 2) {
                String messageText = parts[1];
                if (messageText.length() > maxLength) {
                    maxLength = messageText.length();
                    longestMessage = messageText;
                }
            }
        }

        return "Longest message: " + longestMessage + " (Length: " + maxLength + " characters)";
    }

    // 2c. Search for a message ID and display corresponding recipient and message
    public String searchMessageByID(String searchID) {
        for (int i = 0; i < messageIDs.size(); i++) {
            if (messageIDs.get(i).equals(searchID)) {
                // Find which array contains this message
                String result = searchInMessageArrays(i);
                if (result != null) {
                    return "Message found - " + result;
                }
            }
        }
        return "Message ID not found: " + searchID;
    }

    private String searchInMessageArrays(int index) {
        // This is a simplified search - in a real implementation, you'd have proper mapping
        if (index < sentMessages.size()) {
            String[] parts = sentMessages.get(index).split("\\|");
            return "Recipient: " + parts[0] + " | Message: " + parts[1];
        } else if (index < sentMessages.size() + storedMessages.size()) {
            int storedIndex = index - sentMessages.size();
            String[] parts = storedMessages.get(storedIndex).split("\\|");
            return "Recipient: " + parts[0] + " | Message: " + parts[1];
        } else {
            int disregardedIndex = index - sentMessages.size() - storedMessages.size();
            String[] parts = disregardedMessages.get(disregardedIndex).split("\\|");
            return "Recipient: " + parts[0] + " | Message: " + parts[1];
        }
    }

    // 2d. Search for all messages sent to a particular recipient
    public String searchMessagesByRecipient(String recipient) {
        List<String> results = new ArrayList<>();

        // Search in sent messages
        for (String message : sentMessages) {
            String[] parts = message.split("\\|");
            if (parts.length >= 2 && parts[0].equals(recipient)) {
                results.add("SENT: " + parts[1]);
            }
        }

        // Search in stored messages
        for (String message : storedMessages) {
            String[] parts = message.split("\\|");
            if (parts.length >= 2 && parts[0].equals(recipient)) {
                results.add("STORED: " + parts[1]);
            }
        }

        // Search in disregarded messages
        for (String message : disregardedMessages) {
            String[] parts = message.split("\\|");
            if (parts.length >= 2 && parts[0].equals(recipient)) {
                results.add("DISREGARDED: " + parts[1]);
            }
        }

        if (results.isEmpty()) {
            return "No messages found for recipient: " + recipient;
        }

        StringBuilder result = new StringBuilder();
        result.append("Messages for ").append(recipient).append(":\n");
        for (String msg : results) {
            result.append(msg).append("\n");
        }
        return result.toString();
    }

    // 2e. Delete a message using message hash
    public String deleteMessageByHash(String messageHash) {
        for (int i = 0; i < messageHashes.size(); i++) {
            if (messageHashes.get(i).equals(messageHash)) {
                // Store the message content before deleting
                String deletedMessageContent = "";

                // Remove from appropriate message array
                if (i < sentMessages.size()) {
                    String[] parts = sentMessages.get(i).split("\\|");
                    deletedMessageContent = parts[1];
                    sentMessages.remove(i);
                } else if (i < sentMessages.size() + storedMessages.size()) {
                    int storedIndex = i - sentMessages.size();
                    String[] parts = storedMessages.get(storedIndex).split("\\|");
                    deletedMessageContent = parts[1];
                    storedMessages.remove(storedIndex);
                } else {
                    int disregardedIndex = i - sentMessages.size() - storedMessages.size();
                    String[] parts = disregardedMessages.get(disregardedIndex).split("\\|");
                    deletedMessageContent = parts[1];
                    disregardedMessages.remove(disregardedIndex);
                }

                // Remove the hash and ID
                messageHashes.remove(i);
                messageIDs.remove(i);

                return "Message successfully deleted: " + deletedMessageContent;
            }
        }
        return "Message hash not found: " + messageHash;
    }

    // 2f. Display report that lists full details of all sent messages
    public String displayMessageReport() {
        if (sentMessages.isEmpty() && storedMessages.isEmpty() && disregardedMessages.isEmpty()) {
            return "No messages to display in report.";
        }

        StringBuilder report = new StringBuilder();
        report.append("=== COMPLETE MESSAGE REPORT ===\n");

        // Sent Messages
        if (!sentMessages.isEmpty()) {
            report.append("\n--- SENT MESSAGES ---\n");
            for (int i = 0; i < sentMessages.size(); i++) {
                String[] parts = sentMessages.get(i).split("\\|");
                if (parts.length >= 2) {
                    report.append("Message ").append(i + 1).append(":\n");
                    report.append("  Message Hash: ").append(i < messageHashes.size() ? messageHashes.get(i) : "N/A").append("\n");
                    report.append("  Recipient: ").append(parts[0]).append("\n");
                    report.append("  Message: ").append(parts[1]).append("\n");
                    report.append("  Status: ").append(parts.length >= 3 ? parts[2] : "Sent").append("\n");
                    report.append("----------------------------\n");
                }
            }
        }

        // Stored Messages
        if (!storedMessages.isEmpty()) {
            report.append("\n--- STORED MESSAGES ---\n");
            for (int i = 0; i < storedMessages.size(); i++) {
                String[] parts = storedMessages.get(i).split("\\|");
                if (parts.length >= 2) {
                    int hashIndex = sentMessages.size() + i;
                    report.append("Message ").append(i + 1).append(":\n");
                    report.append("  Message Hash: ").append(hashIndex < messageHashes.size() ? messageHashes.get(hashIndex) : "N/A").append("\n");
                    report.append("  Recipient: ").append(parts[0]).append("\n");
                    report.append("  Message: ").append(parts[1]).append("\n");
                    report.append("  Status: ").append(parts.length >= 3 ? parts[2] : "Stored").append("\n");
                    report.append("----------------------------\n");
                }
            }
        }

        // Disregarded Messages
        if (!disregardedMessages.isEmpty()) {
            report.append("\n--- DISREGARDED MESSAGES ---\n");
            for (int i = 0; i < disregardedMessages.size(); i++) {
                String[] parts = disregardedMessages.get(i).split("\\|");
                if (parts.length >= 2) {
                    int hashIndex = sentMessages.size() + storedMessages.size() + i;
                    report.append("Message ").append(i + 1).append(":\n");
                    report.append("  Message Hash: ").append(hashIndex < messageHashes.size() ? messageHashes.get(hashIndex) : "N/A").append("\n");
                    report.append("  Recipient: ").append(parts[0]).append("\n");
                    report.append("  Message: ").append(parts[1]).append("\n");
                    report.append("  Status: ").append(parts.length >= 3 ? parts[2] : "Disregarded").append("\n");
                    report.append("----------------------------\n");
                }
            }
        }

        return report.toString();
    }

    // ChatGPT-assisted method to read JSON file into array
    /**
     * Reads messages from JSON file into storedMessages array
     * OpenAI (2024) ChatGPT (4o) used for JSON file reading implementation
     */
    public String readMessagesFromJSON(String filename) {
        try {
            File file = new File(filename);
            if (!file.exists()) {
                return "JSON file not found: " + filename;
            }

            BufferedReader reader = new BufferedReader(new FileReader(file));
            Type listType = new TypeToken<List<String>>(){}.getType();
            List<String> jsonMessages = gson.fromJson(reader, listType);
            reader.close();

            if (jsonMessages != null) {
                for (String jsonMessage : jsonMessages) {
                    String[] parts = jsonMessage.split("\\|");
                    if (parts.length >= 3) {
                        addMessageToArray(storedMessages, parts[0], parts[1], parts[2]);
                    }
                }
                return "Successfully read " + jsonMessages.size() + " messages from JSON file.";
            } else {
                return "No messages found in JSON file.";
            }
        } catch (Exception e) {
            return "Error reading JSON file: " + e.getMessage();
        }
    }

    // Helper methods
    private String generateMessageID() {
        return String.valueOf(new Random().nextInt(900000000) + 100000000);
    }

    private String generateMessageHash(String messageID, String message) {
        String firstTwoID = messageID.length() >= 2 ? messageID.substring(0, 2) : messageID;
        String[] words = message.split(" ");
        String firstWord = words.length > 0 ? words[0] : "";
        String lastWord = words.length > 1 ? words[words.length - 1] : firstWord;

        return (firstTwoID + ":" + firstWord + lastWord).toUpperCase();
    }

    // Getters for unit testing
    public List<String> getSentMessages() { return new ArrayList<>(sentMessages); }
    public List<String> getDisregardedMessages() { return new ArrayList<>(disregardedMessages); }
    public List<String> getStoredMessages() { return new ArrayList<>(storedMessages); }
    public List<String> getMessageHashes() { return new ArrayList<>(messageHashes); }
    public List<String> getMessageIDs() { return new ArrayList<>(messageIDs); }

    // Method to get the actual longest message for testing
    public String getLongestMessageText() {
        String longestMessage = "";
        int maxLength = 0;

        // Check all message arrays
        List<List<String>> allMessages = Arrays.asList(sentMessages, storedMessages, disregardedMessages);

        for (List<String> messageList : allMessages) {
            for (String messageEntry : messageList) {
                String[] parts = messageEntry.split("\\|");
                if (parts.length >= 2) {
                    String messageText = parts[1];
                    if (messageText.length() > maxLength) {
                        maxLength = messageText.length();
                        longestMessage = messageText;
                    }
                }
            }
        }

        return longestMessage;
    }
}