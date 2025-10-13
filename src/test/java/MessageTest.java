import org.example.Message;
import org.junit.Test;
import static org.junit.Assert.*;

public class MessageTest {



    @Test
    public void testCheckMessageID() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight.");
        assertTrue("Message ID should be 10 characters or less", msg.checkMessageID());
    }

    @Test
    public void testCheckRecipientCellCorrectFormat() {
        Message msg = new Message("+27718693002", "Test message");
        assertTrue("Recipient cell should be correctly formatted", msg.checkRecipientCell());
    }

    @Test
    public void testCheckRecipientCellIncorrectFormat() {
        Message msg = new Message("08575975889", "Test message");
        assertFalse("Recipient cell should be incorrectly formatted", msg.checkRecipientCell());
    }

    @Test
    public void testCreateMessageHash() {
        Message msg = new Message("+27718693002", "Hi Mike, can you join us for dinner tonight.");
        String hash = msg.createMessageHash();
        assertNotNull("Message hash should not be null", hash);
        assertTrue("Message hash should contain colon separators", hash.contains(":"));
    }

    @Test
    public void testMessageLengthWithinLimit() {
        Message msg = new Message("+27718693002", "Short message");
        assertTrue("Message should be within 250 characters", msg.checkMessageLength());
    }

    @Test
    public void testMessageLengthExceedsLimit() {
        // Create a long message that exceeds 250 characters
        String longMessage = "This is a very long message that definitely exceeds the 250 character limit. "
                + "This is a very long message that definitely exceeds the 250 character limit. "
                + "This is a very long message that definitely exceeds the 250 character limit. "
                + "This is a very long message that definitely exceeds the 250 character limit.";
        Message msg = new Message("+27718693002", longMessage);
        assertFalse("Message should exceed 250 characters", msg.checkMessageLength());
    }

    @Test
    public void testGetMessageLengthMessageSuccess() {
        Message msg = new Message("+27718693002", "Short message");
        assertEquals("Message ready to send.", msg.getMessageLengthMessage());
    }

    @Test
    public void testGetMessageLengthMessageFailure() {
        String longMessage = "This is a very long message that definitely exceeds the 250 character limit. "
                + "This is a very long message that definitely exceeds the 250 character limit. "
                + "This is a very long message that definitely exceeds the 250 character limit. "
                + "This is a very long message that definitely exceeds the 250 character limit.";
        Message msg = new Message("+27718693002", longMessage);
        assertTrue(msg.getMessageLengthMessage().contains("Message exceeds 250 characters by"));
    }

    @Test
    public void testGetRecipientValidationMessageSuccess() {
        Message msg = new Message("+27718693002", "Test message");
        assertEquals("Cell phone number successfully captured.", msg.getRecipientValidationMessage());
    }

    @Test
    public void testGetRecipientValidationMessageFailure() {
        Message msg = new Message("08575975889", "Test message");
        assertEquals("Cell phone number is incorrectly formatted or does not contain an international code. Please correct the number and try again.",
                msg.getRecipientValidationMessage());
    }

    @Test
    public void testSentMessageSend() {
        Message msg = new Message("+27718693002", "Test message");
        String result = msg.sentMessage(1); // Send a message
        assertEquals("Message successfully sent.", result);
    }

    @Test
    public void testSentMessageStore() {
        Message msg = new Message("+27718693002", "Test message");
        String result = msg.sentMessage(3); // Store message
        assertEquals("Message successfully stored.", result);
    }

    @Test
    public void testSentMessageDisregard() {
        Message msg = new Message("+27718693002", "Test message");
        String result = msg.sentMessage(2); // Disregard message
        assertEquals("Press 0 to delete message.", result);
    }

    @Test
    public void testReturnTotalMessages() {
        // Reset static counter for clean test
        Message msg1 = new Message("+27718693002", "Message 1");
        msg1.sentMessage(1); // Send the first message

        Message msg2 = new Message("+27838968976", "Message 2");
        msg2.sentMessage(1); // Send the second message

        assertEquals(2, Message.returnTotalMessages());
    }
}