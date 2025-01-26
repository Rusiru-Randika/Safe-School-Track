import jakarta.mail.MessagingException;

public class Main {
    public static void main(String[] args) throws MessagingException {
        DatabaseManager.connectToDatabase();
        DatabaseManager.createParentTable();
        LoginManager.login();


    }


}
