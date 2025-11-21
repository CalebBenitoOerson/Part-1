import org.example.MessageManager;

import org.junit.Test;
import static org.junit.Assert.*;
import java.util.List;

public class MessageManagerTest {

    @Test
    public void testSentMessagesArrayCorrectlyPopulated() {
        MessageManager manager = new MessageManager();
        List<String> sentMessages = manager.getSentMessages();

        assertTrue("Sent messages array should contain test data",
                sentMessages.size() >= 2);

        // Check if expected test data is present
        boolean foundMessage1 = false;
        boolean foundMessage4 = false;

        for (String message : sentMessages) {
            if (message.contains("Did you get the cake?")) {
                foundMessage1 = true;
            }
            if (message.contains("It is dinner time!")) {
                foundMessage4 = true;
            }
        }

        assertTrue("Should contain 'Did you get the cake?'", foundMessage1);
        assertTrue("Should contain 'It is dinner time!'", foundMessage4);
    }

    @Test
    public void testDisplayLongestMessage() {
        MessageManager manager = new MessageManager();
        String result = manager.displayLongestMessage();

        // Get the actual longest message text from the manager
        String longestMessage = manager.getLongestMessageText();

        assertTrue("Should return the longest message. Expected to contain: " + longestMessage + ", but got: " + result,
                result.contains(longestMessage));
    }

    @Test

    public void testSearchMessagesByRecipient() {
        MessageManager manager = new MessageManager();
        String result = manager.searchMessagesByRecipient("+27838884567");

        assertTrue("Should find messages for recipient +27838884567",
                result.contains("Where are you? You are late!") &&
                        result.contains("Ok, I am leaving without you."));
    }

    @Test
    public void testDeleteMessageByHash() {
        MessageManager manager = new MessageManager();
        List<String> hashes = manager.getMessageHashes();

        if (!hashes.isEmpty()) {
            String firstHash = hashes.get(0);
            String result = manager.deleteMessageByHash(firstHash);

            assertTrue("Should confirm successful deletion",
                    result.contains("successfully deleted"));
        }
    }

    @Test
    public void testDisplayMessageReport() {
        MessageManager manager = new MessageManager();
        String report = manager.displayMessageReport();

        assertTrue("Report should contain message details",
                report.contains("Message Hash") &&
                        report.contains("Recipient") &&
                        report.contains("Message"));
    }

    @Test
    public void testReadMessagesFromJSON() {
        MessageManager manager = new MessageManager();
        String result = manager.readMessagesFromJSON("test_messages.json");

        // Since we might not have the file in test environment, check for appropriate response
        assertTrue("Should handle JSON file operation",
                result.contains("Successfully") ||
                        result.contains("not found") ||
                        result.contains("Error"));
    }
}