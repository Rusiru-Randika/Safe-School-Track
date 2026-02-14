import jakarta.mail.MessagingException;

public class Main {
    public static void main(String[] args) {
        DatabaseManager.connectToDatabase();
        DatabaseManager.createParentTable();

        try {
            LoginManager.login();
        } catch (MessagingException e) {
            System.out.println("Email error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Unexpected error: " + e.getMessage());
        }
    }
}
