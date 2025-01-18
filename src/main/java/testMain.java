
import java.util.List;

public class testMain {
    public static void main(String[] args) {
        // Step 1: Connect to the database
        DatabaseManager.connectToDatabase();
        DatabaseManager.createParentTable();
        //..........
        LoginManager.login();

        // Accessing Login_status of LoginManager
        if (LoginManager.getLoginStatus()) {
            System.out.println("User is logged in.");
        } else {
            System.out.println("User is not logged in.");
        }
        //...........


        DatabaseManager.closeConnection();

    }

}
