import jakarta.mail.MessagingException;
import java.util.List;
import java.util.Scanner;

class LoginManager {
    private static boolean loginStatus = false;

    public static boolean getLoginStatus() {
        return loginStatus;
    }

    public static void login() throws MessagingException {
        System.out.println("\n________________________________Welcome to Safe School Track_________________________________\n");
        Scanner scn = new Scanner(System.in);
        int option = 0;

        while (option != 4) {
            System.out.println("Select an option: \n 1) Login as Parent \n 2) Create new account \n 3) Login as Driver \n 4) Exit");
            System.out.print("\nSelected option: ");

            if (scn.hasNextInt()) {
                option = scn.nextInt();

                switch (option) {
                    case 1:
                        parentLogin(scn);
                        break;
                    case 2:
                        CreateNewUser.createUser();
                        break;
                    case 3:
                        driverLogin(scn);
                        break;
                    case 4:
                        System.out.println("Thank you for using Safe School Track. Exiting now...");
                        System.out.println("_______________________________See you again!_______________________________\n");
                        DatabaseManager.closeConnection();
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Enter a valid option\n");
                        break; // Fixed: was recursively calling login() which can cause StackOverflow
                }
            } else {
                System.out.println("Invalid input. Please enter a number.\n");
                scn.next(); // Clear invalid input
            }
        }
    }

    private static void parentLogin(Scanner scn) throws MessagingException {
        System.out.println("\n___________________________________Parent Login____________________________________\n");

        while (true) {
            System.out.print("Enter username: ");
            String username = scn.next();
            int userId = DatabaseManager.giveUserGetId(username);

            if (userId > 0) {
                if (processParentPassword(scn, username, userId)) {
                    return;
                }
            } else {
                System.out.println("* Invalid username entered *");
                if (!retryOrGoBack(scn)) return;
            }
        }
    }

    private static boolean processParentPassword(Scanner scn, String username, int userId) throws MessagingException {
        while (true) {
            System.out.print("Enter password: ");
            String inputPassword = scn.next();
            String storedPassword = DatabaseManager.giveUserGetPassword(username);

            if (storedPassword == null) {
                System.out.println("\t* Error retrieving password. Please try again later. *");
                return false;
            }

            if (storedPassword.equals(inputPassword)) {
                System.out.println("Checking...");
                ParentManager parent = DatabaseManager.getParentObject(userId);
                System.out.println("\t*** Hello! " + parent.getName() + ", You are logged in! ***");
                loginStatus = true;
                displayAndEditData(scn, parent);
                return true;
            } else {
                System.out.println("\t* Wrong password! *");
                if (!retryOrGoBack(scn)) return false;
            }
        }
    }

    private static boolean retryOrGoBack(Scanner scn) {
        int retryOption = 0;
        while (retryOption != 1 && retryOption != 2) {
            System.out.print("Do you want to try again or go back? (1: Try again, 2: Go back): ");
            if (scn.hasNextInt()) {
                retryOption = scn.nextInt();
                if (retryOption == 2) return false;
            } else {
                System.out.println("Invalid input. Please enter 1 or 2.");
                scn.next();
            }
        }
        return true;
    }

    private static void displayAndEditData(Scanner scn, ParentManager parent) throws MessagingException {
        List<Integer> childIds = DatabaseManager.giveParentIdGetStuId(parent.getId());

        if (childIds.isEmpty()) {
            System.out.println("No child details found.");
            return;
        }

        System.out.println("Your Data in the Database: ");
        System.out.println(parent.toString());
        System.out.println();

        for (Integer id : childIds) {
            printStudentSummary(id);
        }

        while (true) {
            System.out.print("1. Edit Any Student Details\n2. Edit Parent Details\n3. Exit\nEnter Your Choice: ");
            int choice = scn.nextInt();
            scn.nextLine();

            switch (choice) {
                case 1:
                    System.out.println("___________________________________________________________________________________________");
                    System.out.println("Your child id with name:");
                    for (int childId : childIds) {
                        String[] childData = DatabaseManager.getStuData(childId);
                        System.out.println("--------------");
                        System.out.printf("ID: %d  | Name: %s%n", childId, childData[0]);
                    }
                    editStudentDetails(scn, childIds, parent);
                    break;
                case 2:
                    editParentDetails(scn, parent);
                    break;
                case 3:
                    return;
                default:
                    System.out.println("Invalid input. Please enter 1, 2, or 3.");
                    break;
            }
        }
    }

    private static void printStudentSummary(int id) {
        String[] data = DatabaseManager.getStuData(id);
        if (data != null && data.length == 7 && data[0] != null) {
            System.out.println("Student Name: " + data[0]);
            System.out.println("Age: " + (data[1] != null ? data[1] : "-"));
            System.out.println("Address: " + (data[2] != null ? data[2] : "-"));
            System.out.println("School: " + (data[3] != null ? data[3] : "-"));
            System.out.println("Teacher Number: " + (data[4] != null ? data[4] : "-"));
            System.out.println("Status: " + (data[5] != null ? data[5] : "-"));
            System.out.println("Parent ID: " + (data[6] != null ? data[6] : "-"));
        } else {
            System.out.println("Invalid Data for Student ID: " + id);
        }
        System.out.println("--------------------------------------------");
    }

    public static void editStudentDetails(Scanner scn, List<Integer> validIds, ParentManager parent) {
        int id;
        while (true) {
            System.out.print("Enter Student ID to update: ");
            id = scn.nextInt();
            scn.nextLine();
            if (validIds.contains(id)) {
                break;
            }
            System.out.println("Invalid Student ID. Please enter one of the displayed IDs.");
        }

        printStudentTable(id);

        boolean continueEditing = true;
        while (continueEditing) {
            System.out.println("Select the detail to update:");
            System.out.println("1. Name\n2. Age\n3. Address\n4. School\n5. Teacher Number\n6. Delete Student account\n7. Send Driver a Message\n8. Done");
            System.out.print("Enter option: ");
            int option = scn.nextInt();
            scn.nextLine();

            switch (option) {
                case 1: {
                    System.out.print("Enter New Student Name: ");
                    DatabaseManager.updateStuField(id, "name", scn.nextLine());
                    break;
                }
                case 2: {
                    int age = readValidInt(scn, "Enter New Age: ", "Invalid input. Please enter a valid age.");
                    DatabaseManager.updateStuField(id, "age", age);
                    break;
                }
                case 3: {
                    System.out.print("Enter New Address: ");
                    DatabaseManager.updateStuField(id, "address", scn.nextLine());
                    break;
                }
                case 4: {
                    System.out.print("Enter New School: ");
                    DatabaseManager.updateStuField(id, "school", scn.nextLine());
                    break;
                }
                case 5: {
                    String teacherNum = readValidPhone(scn);
                    DatabaseManager.updateStuField(id, "teacherNum", teacherNum);
                    break;
                }
                case 6: {
                    System.out.print("Are you sure you want to remove the student? (yes/no): ");
                    String confirmation = scn.nextLine().toLowerCase();
                    if (confirmation.equals("yes")) {
                        removeStudent(scn, parent);
                        System.out.println("Student removed successfully.");
                        System.out.println("You will be redirected to the login screen now...");
                        try {
                            LoginManager.login();
                        } catch (Exception e) {
                            System.out.println("Error during login: " + e.getMessage());
                        }
                    } else {
                        System.out.println("Operation cancelled. Returning to the previous menu.");
                    }
                    return;
                }
                case 7: {
                    handleDriverMessage(scn, id);
                    break; // Fixed: was falling through into case 8
                }
                case 8: {
                    continueEditing = false;
                    break;
                }
                default: {
                    System.out.println("Invalid option selected. Please enter a number between 1 and 8.");
                    break;
                }
            }

            if (continueEditing) {
                printStudentTable(id);
            }
        }
        System.out.println("Student details updated in the database.");
    }

    private static void handleDriverMessage(Scanner scn, int studentId) {
        int option = 0;
        while (option < 1 || option > 3) {
            System.out.println("Select Driver Message:");
            System.out.println("1. Don't pick at home today.");
            System.out.println("2. Don't pick at school afternoon.");
            System.out.println("3. Type special message");
            System.out.print("Enter option: ");

            if (scn.hasNextInt()) {
                option = scn.nextInt();
                scn.nextLine();
                if (option < 1 || option > 3) {
                    System.out.println("Invalid option. Please enter 1, 2, or 3.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number.");
                scn.next();
            }
        }

        String newStatus = switch (option) {
            case 1 -> "Parent Message - Don't pick at home today.";
            case 2 -> "Parent Message - Don't pick at school afternoon.";
            case 3 -> {
                System.out.print("Type special message: ");
                yield "Parent Message - " + scn.nextLine();
            }
            default -> null;
        };

        if (newStatus != null) {
            boolean result = DatabaseManager.updateStuField(studentId, "Student_Status", newStatus);
            System.out.println(result ? "Message to Driver updated successfully." : "Failed to update.");
        }
    }

    private static void printStudentTable(int id) {
        String[] data = DatabaseManager.getStuData(id);
        System.out.println("+------------------+------------------+------------------+------------------+------------------+-------------------------------------------------------+------------------+");
        System.out.println("| Student Name     | Age              | Address          | School           | Teacher Number   | Status                                                | Parent ID        |");
        System.out.println("+------------------+------------------+------------------+------------------+------------------+-------------------------------------------------------+------------------+");
        System.out.printf("| %-16s | %-16s | %-16s | %-16s | %-16s | %-55s| %-16s |%n",
                data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
        System.out.println("+------------------+------------------+------------------+------------------+------------------+-------------------------------------------------------+------------------+");
    }

    private static void editParentDetails(Scanner scn, ParentManager parent) throws MessagingException {
        boolean continueEditing = true;

        while (continueEditing) {
            System.out.println("Information in the database : " + parent.toString());
            System.out.println("Select the detail to update:");
            System.out.println("1. Name\n2. Email\n3. Phone\n4. Add Student\n5. Remove Parent Account\n6. Done");
            System.out.print("Enter option: ");
            int option = scn.nextInt();
            scn.nextLine();

            switch (option) {
                case 1: {
                    System.out.print("Enter New Parent Name: ");
                    parent.setName(scn.nextLine());
                    break;
                }
                case 2: {
                    System.out.print("Enter New Email: ");
                    parent.setEmail(scn.nextLine());
                    break;
                }
                case 3: {
                    String phone = readValidPhone(scn);
                    parent.setPhone(phone);
                    break;
                }
                case 4: {
                    addStudent(scn, parent);
                    break;
                }
                case 5: {
                    DatabaseManager.deleteParentData(parent.getId());
                    System.out.println("Account deleted. Redirecting to main menu...");
                    login();
                    return;
                }
                case 6: {
                    continueEditing = false;
                    break;
                }
                default: {
                    System.out.println("Invalid option. Please try again.");
                    break;
                }
            }
        }
    }

    private static void addStudent(Scanner scn, ParentManager parent) {
        System.out.print("Enter Student Name: ");
        String stuName = scn.nextLine();

        int stuAge = readValidInt(scn, "Enter Student Age: ", "Invalid input. Please enter a valid age.");

        System.out.print("Enter Student Address: ");
        String stuAddress = scn.nextLine();

        System.out.print("Enter Student School: ");
        String stuSchool = scn.nextLine();

        System.out.print("Enter Teacher Number (10 digits): ");
        String teacherNum = scn.next();

        DatabaseManager.insertStuData(stuName, stuAge, stuAddress, stuSchool, teacherNum, "active", parent.getId());
        parent.setNumberOfStudents(parent.getNumberOfStudents() + 1);
    }

    private static void removeStudent(Scanner scn, ParentManager parent) {
        System.out.println("This Cannot Be Undone, Are you Sure? (1-yes, 2-no)");
        if (scn.nextInt() != 1) {
            return;
        }

        List<Integer> childIds = DatabaseManager.giveParentIdGetStuId(parent.getId());
        if (childIds.isEmpty()) {
            System.out.println("No child details found.");
            return;
        }

        System.out.println("Your child details:");
        for (int childId : childIds) {
            String[] childData = DatabaseManager.getStuData(childId);
            System.out.printf("ID: %d, Name: %s%n", childId, childData[0]);
        }
        System.out.print("Enter Student ID to remove: ");
        int stuId = scn.nextInt();
        DatabaseManager.deleteStuData(stuId);
        parent.setNumberOfStudents(parent.getNumberOfStudents() - 1);
    }

    // =========================================================================
    // Driver Login & Operations
    // =========================================================================

    private static void driverLogin(Scanner scn) {
        System.out.println("\n___________________________________Driver Login____________________________________\n");

        while (true) {
            System.out.print("Enter username: ");
            String username = scn.next();

            if (!DatabaseManager.isDriverInDb(username)) {
                System.out.println("* Invalid username entered *");
                if (!retryOrGoBack(scn)) return;
                continue;
            }

            Integer userId = DatabaseManager.giveDriverGetId(username);
            if (userId != null && userId > 0) {
                if (processDriverPassword(scn, username, userId)) {
                    return;
                }
            } else {
                System.out.println("* Invalid username entered *");
                if (!retryOrGoBack(scn)) return;
            }
        }
    }

    private static boolean processDriverPassword(Scanner scn, String username, int id) {
        while (true) {
            System.out.print("Enter password: ");
            String inputPassword = scn.next();
            String storedPassword = DatabaseManager.giveDriverGetPassword(username);

            if (storedPassword == null) {
                System.out.println("\t* Error retrieving password. Please try again later. *");
                return false;
            }

            if (storedPassword.equals(inputPassword)) {
                System.out.println("Checking...");
                Driver driver = DatabaseManager.getDriverObject(id);
                if (driver.getId() != 0) {
                    System.out.println("\t*** Hello! " + driver.getName() + ", You are logged in! ***");
                    loginStatus = true;
                    System.out.println("Your details:\n" + driver.toString());
                    chooseDriverAction(scn, driver);
                    return true;
                } else {
                    System.out.println("\t* Driver details not found. Please contact support. *");
                    return false;
                }
            } else {
                System.out.println("\t* Wrong password! *");
                if (!retryOrGoBack(scn)) return false;
            }
        }
    }

    private static void driverDataUpdate(Scanner scn, Driver driver) {
        boolean continueUpdating = true;
        while (continueUpdating) {
            System.out.println("\nCurrent Driver Details:");
            System.out.println(driver.toString());
            System.out.println("\nSelect the field to update:");
            System.out.println("1. Name\n2. Password\n3. Email\n4. Phone\n5. Van Number\n6. Delete Account\n7. Exit");
            System.out.print("Enter your choice: ");

            int choice = readValidInt(scn, "", "Invalid input. Please enter a valid integer.");

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    driver.setName(scn.nextLine());
                    System.out.println("Name updated successfully!");
                    break;
                case 2:
                    System.out.print("Enter new password: ");
                    driver.setPassword(scn.nextLine());
                    System.out.println("Password updated successfully!");
                    break;
                case 3:
                    System.out.print("Enter new email: ");
                    driver.setEmail(scn.nextLine());
                    System.out.println("Email updated successfully!");
                    break;
                case 4:
                    System.out.print("Enter new phone number: ");
                    driver.setPhone(scn.nextLine());
                    System.out.println("Phone number updated successfully!");
                    break;
                case 5:
                    System.out.print("Enter new van number: ");
                    driver.setVanNumber(scn.nextLine());
                    System.out.println("Van number updated successfully!");
                    break;
                case 6:
                    System.out.print("Are you sure you want to delete this account? (Y/N): ");
                    String confirmation = scn.nextLine().trim().toUpperCase();
                    if (confirmation.equals("Y")) {
                        DatabaseManager.deleteDriverData(driver.getId());
                        System.out.println("Account deleted successfully. Redirecting to login...");
                        try {
                            LoginManager.login();
                        } catch (Exception e) {
                            System.out.println("Error during login: " + e.getMessage());
                        }
                        return;
                    } else {
                        System.out.println("Account deletion canceled.");
                    }
                    break;
                case 7:
                    continueUpdating = false;
                    System.out.println("Exiting update menu.");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

    private static void chooseDriverAction(Scanner scn, Driver driver) {
        boolean running = true;
        while (running) {
            System.out.println("\nChoose from:");
            System.out.println("1. Update Account Details\n2. Change Student Status\n3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scn.nextInt();
            scn.nextLine();

            switch (choice) {
                case 1:
                    driverDataUpdate(scn, driver);
                    break;
                case 2:
                    System.out.println("------------------------Student Status Update section----------------------");
                    StudentManager.sortFromSchool(scn);
                    break;
                case 3:
                    running = false;
                    System.out.println("Exiting the menu. Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

    // =========================================================================
    // Input Validation Helpers
    // =========================================================================

    static int readValidInt(Scanner scn, String prompt, String errorMsg) {
        while (true) {
            if (!prompt.isEmpty()) System.out.print(prompt);
            if (scn.hasNextInt()) {
                int value = scn.nextInt();
                scn.nextLine();
                return value;
            } else {
                System.out.println(errorMsg);
                scn.next();
            }
        }
    }

    static String readValidPhone(Scanner scn) {
        while (true) {
            System.out.print("Enter Phone Number (10 digits): ");
            String phone = scn.next();
            if (phone.matches("\\d{10}")) {
                return phone;
            }
            System.out.println("Invalid input. Please enter a valid 10-digit phone number.");
        }
    }
}
