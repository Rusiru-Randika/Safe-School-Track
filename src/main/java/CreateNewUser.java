import jakarta.mail.MessagingException;
import java.util.Scanner;
import java.util.ArrayList;

class CreateNewUser {

    public static void createUser() throws MessagingException {
        Scanner scn = new Scanner(System.in);

        System.out.println("\n________________________Create a new user _______________________\n");
        System.out.println("Select your role: \n1- Parent \n2- Driver");
        int role = scn.nextInt();
        scn.nextLine();

        switch (role) {
            case 1 -> createParentAccount(scn);
            case 2 -> createDriverAccount(scn);
            default -> {
                System.out.println("Data not entered! Please try again.");
                LoginManager.login();
            }
        }
    }

    // =========================================================================
    // Parent Account Creation
    // =========================================================================

    private static void createParentAccount(Scanner scn) throws MessagingException {
        System.out.print("Enter Name: ");
        String name = scn.nextLine();

        String email = readNoSpaces(scn, "Enter e-mail: ");
        String phone = LoginManager.readValidPhone(scn);

        String username = readUniqueUsername(scn);
        String password = readConfirmedPassword(scn);

        int noStu = LoginManager.readValidInt(scn, "Enter Number of students: ",
                "Invalid input. Please enter a valid number of students.");

        int id = DatabaseManager.insertParentData(name, username, password, email, phone, noStu);
        if (id > 0) {
            System.out.println("\n\t*** User created successfully ***\n");
            sendWelcomeEmail(email);
        }

        if (noStu > 0) {
            addStudentsForParent(scn, username, noStu);
        } else {
            System.out.println("\n\t* No students *\n");
        }
    }

    private static void addStudentsForParent(Scanner scn, String username, int noStu) {
        System.out.println("\n_________________________ Let's add student details _________________________\n");
        int parentId = DatabaseManager.giveUserGetId(username);
        ArrayList<Integer> studentIds = new ArrayList<>();

        for (int i = 0; i < noStu; i++) {
            System.out.println("\nStudent " + (i + 1) + " details:\n");

            System.out.print("Enter Name: ");
            String stuName = scn.nextLine();

            int stuAge = LoginManager.readValidInt(scn, "Enter Age: ",
                    "Invalid input. Please enter a valid age.");

            System.out.print("Enter Address: ");
            String stuAddress = scn.nextLine();

            System.out.print("Enter School: ");
            String stuSchool = scn.nextLine();

            String teacherPhone = readValidTeacherPhone(scn);

            int stuId = DatabaseManager.insertStuData(stuName, stuAge, stuAddress, stuSchool, teacherPhone, "dropped", parentId);
            studentIds.add(stuId);
            if (stuId > 0) {
                System.out.println("\n\t* Added student " + (i + 1) + " successfully *\n");
            }
        }

        System.out.println("\t*** Successfully added student details ***\n");
        for (int i = 0; i < studentIds.size(); i++) {
            System.out.println("Student " + (i + 1) + " ID: " + studentIds.get(i));
        }
        System.out.println();
    }

    // =========================================================================
    // Driver Account Creation
    // =========================================================================

    private static void createDriverAccount(Scanner scn) throws MessagingException {
        int driverCount = DatabaseManager.getDriverRowCount();
        if (driverCount >= 1) {
            System.out.println(driverCount == 1
                    ? "New driver accounts cannot be created because the system allows only one driver account, and an account already exists."
                    : "Contact Technical Support. Going back.");
            LoginManager.login();
            return;
        }

        System.out.print("Enter Name: ");
        String name = scn.nextLine();

        String email = readNoSpaces(scn, "Enter e-mail: ");
        String phone = LoginManager.readValidPhone(scn);
        String username = readUniqueUsername(scn);

        // For driver, passwords must match or the process restarts
        System.out.print("Enter Password: ");
        String password = readNoSpaces(scn, "");
        System.out.print("Enter Password again: ");
        String confirmPassword = readNoSpaces(scn, "");

        if (!password.equals(confirmPassword)) {
            System.out.println("\t* Passwords do not match. Restarting process. *\n");
            return;
        }

        System.out.print("Enter Van Number: ");
        String vanNumber = scn.nextLine();
        if (vanNumber.isEmpty()) {
            System.out.println("Van number cannot be empty!");
            return;
        }

        int id = DatabaseManager.insertDriverData(name, username, password, email, phone, vanNumber);
        if (id > 0) {
            System.out.println("\n\t*** Driver added successfully ***\n");
        } else {
            System.out.println("Failed to add driver. Please try again.");
        }

        LoginManager.login();
    }

    // =========================================================================
    // Input Validation Helpers
    // =========================================================================

    private static String readNoSpaces(Scanner scn, String prompt) {
        if (!prompt.isEmpty()) System.out.print(prompt);
        String input = scn.nextLine();
        while (input.contains(" ")) {
            System.out.println("The data you entered contains spaces. Please enter valid data.");
            input = scn.nextLine();
        }
        return input;
    }

    private static String readUniqueUsername(Scanner scn) {
        String username = readNoSpaces(scn, "Enter Username: ");
        while (DatabaseManager.giveUserGetId(username) > 0) {
            System.out.println("\n\t* Username already taken! *\n");
            username = readNoSpaces(scn, "Enter another Username: ");
        }
        return username;
    }

    private static String readConfirmedPassword(Scanner scn) {
        while (true) {
            String password = readNoSpaces(scn, "Enter Password: ");
            String confirm = readNoSpaces(scn, "Enter Password again: ");
            if (password.equals(confirm)) {
                return password;
            }
            System.out.println("\t* Passwords do not match, try again! *\n");
        }
    }

    private static String readValidTeacherPhone(Scanner scn) {
        while (true) {
            System.out.print("Enter Teacher's phone number (10 digits): ");
            String phone = scn.nextLine();
            if (phone.matches("\\d{10}")) {
                return phone;
            }
            System.out.println("Invalid input. Please enter a valid 10-digit phone number.");
        }
    }

    private static void sendWelcomeEmail(String email) {
        String subject = "Welcome to Safe School Track - Your Account is Ready!";
        String body = "<html><body>"
                + "<h2 style='color:#4CAF50;'>Welcome to Safe School Track</h2>"
                + "<p style='font-size:16px;'>Your account was created successfully.</p>"
                + "<p style='font-size:16px;'>Thank you for joining the Safe School Track system!</p>"
                + "<p style='color:#888;'>If you have any questions, feel free to contact us.</p>"
                + "<footer style='font-size:12px;color:#aaa;'>Safe School Track Team</footer>"
                + "</body></html>";
        try {
            NotificationManager.sendMail(email, subject, body);
        } catch (MessagingException e) {
            System.err.println("\nFailed to send the email.\n");
            e.printStackTrace();
        }
    }
}
