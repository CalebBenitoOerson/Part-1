import org.example.login;
import org.junit.Test;


import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class LoginTest{
    @Test
    public void testUsernameCorrectlyFormatted() {
        login user = new login("Kyle", "Doe", "Kyl_1", "Ch&&s.e@ke99!", "+27836687654");
        assertTrue(user.checkUserName());
    }

    @Test
    public void testUsernameIncorrectlyFormatted() {
        login user = new login("Kyle", "Doe", "kyle!!!!!!!", "Ch&&s.e@ke99!", "+27838968976");
        assertFalse(user.checkUserName());
    }

    @Test
    public void testPasswordMeetsRequirements() {
        login user = new login("Kyle", "Doe", "Kyl_1", "Ch&&s.e@ke99!", "+27838968976");
        assertTrue(user.checkPasswordComplexity());
    }

    @Test
    public void testPasswordDoesNotMeetRequirements() {
        login user = new login("Kyle", "Doe", "Kyl_1", "password", "+27838968976");
        assertFalse(user.checkPasswordComplexity());
    }

    @Test
    public void testCellPhoneCorrectFormat() {
        login user = new login("Kyle", "Doe", "Kyl_1", "Ch&&s.e@ke99!", "+27838968976");
        assertTrue(user.checkCellPhoneNumber());
    }

    @Test
    public void testCellPhoneIncorrectFormat() {
        login user = new login("Kyle", "Doe", "Kyl_1", "Ch&&s.e@ke99!", "08966553");
        assertFalse( user.checkCellPhoneNumber());
    }

    @Test
    public void testLoginSuccessful() {
        login user = new login("Kyle", "Doe", "Kyl_1", "Ch&&s.e@ke99!", "+27838968976");
        assertTrue(user.loginUser("Kyl_1", "Ch&&s.e@ke99!"));
    }

    @Test
    public void testLoginFailed() {
        login user = new login("Kyle", "Doe", "Kyl_1", "Ch&&s.e@ke99!", "+27838968976");
        assertFalse(user.loginUser("WrongUser", "WrongPass"));
    }
}
