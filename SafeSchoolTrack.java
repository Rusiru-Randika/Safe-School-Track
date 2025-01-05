public class SafeSchoolTrack {
    private DatabaseManager dbManager;
    private LoginManager loginManager;
    private UserManager userManager;
    private NotificationManager notificationManager;

    public SafeSchoolTrack() {
        dbManager = new DatabaseManager();
        loginManager = new LoginManager(dbManager);
        userManager = new UserManager(dbManager);
        notificationManager = new NotificationManager(dbManager);
        System.out.println("Safe School Track system initialized.");
    }

    public void run() {
        System.out.println("Welcome to Safe School Track!");

        // Create an admin user for demonstration purposes
        loginManager.createUser("admin", "password123", "admin");
        String role = loginManager.verifyUser("admin", "password123");

        if (role != null) {
            System.out.println("Login successful. Role: " + role);

            // Adding a sample student
            userManager.addStudent("John Doe", "ABC School", "5th Grade");

            try {
                // Retrieve and display student details
                ResultSet student = userManager.getStudent(1);
                if (student.next()) {
                    System.out.println("Student Details: " +
                            student.getInt("student_id") + ", " +
                            student.getString("name") + ", " +
                            student.getString("school") + ", " +
                            student.getString("grade"));
                }

                // Send a sample notification
                notificationManager.sendNotification("admin", "parent1", "Your child has boarded the bus.");

                // Retrieve and display notifications for a parent
                ResultSet notifications = notificationManager.getNotifications("parent1");
                while (notifications.next()) {
                    System.out.println("Notification: " + notifications.getString("message"));
                }
            } catch (SQLException e) {
                System.out.println("Error during runtime operations.");
                e.printStackTrace();
            }
        } else {
            System.out.println("Login failed.");
        }
    }

    public static void main(String[] args) {
        SafeSchoolTrack system = new SafeSchoolTrack();
        system.run();
    }
}
