import jakarta.mail.MessagingException;

public class Main {
    public static void main(String[] args) throws MessagingException {
        // Step 1: Connect to the database
        DatabaseManager.connectToDatabase();
        DatabaseManager.createParentTable();
        // ...existing code...
        LoginManager.login();

        // Accessing Login_status of LoginManager
        if (LoginManager.getLoginStatus()) {
            System.out.println("User is logged in.");
        } else {
            System.out.println("User is not logged out.");
        }
        // ...existing code...

        DatabaseManager.closeConnection();
    }


}
