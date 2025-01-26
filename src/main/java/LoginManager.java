
import jakarta.mail.MessagingException;

import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

class LoginManager {
    private static boolean loginStatus = false;

    public static boolean getLoginStatus() {
        return loginStatus;
    }

    public static void login() throws MessagingException {
        System.out.println("\n________________________________Welcome to Safe School Track_________________________________\n");
        int option = 0;
        Scanner scn = new Scanner(System.in);

        while (option != 4) { // Exit on option 4
            System.out.println("Select an option: \n 1) Login as Parent \n 2) Create new account \n 3) Login as Driver \n 4) Exit");
            System.out.print("\nSelected option: ");

            if (scn.hasNextInt()) {
                option = scn.nextInt();

                switch (option) {
                    case 1: {
                        parentLogin(scn);
                    }
                    break;
                    case 2: {
                        CreateNewUser.CreateUser();
                    }
                    break;
                    case 3: {
                        DriverLogin(scn);
                    }
                    break;
                    case 4: {
                        System.out.println("_______________________________See you again!_______________________________\n");
                    }
                    break;
                    default: {
                        System.out.println("Enter a valid option\n");
                    }
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
                    return; // Login successful
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

            if (storedPassword.equals(inputPassword)) {
                System.out.println("Checking...");
                ParentManager parent = DatabaseManager.getParentObject(userId);
                System.out.println("\t*** Hello! " + parent.getName() + ", You are logged in! ***");
                loginStatus = true;

                displayAndEditData(parent); // Allow parent to manage child details
                return true; // Login successful
            } else {
                System.out.println("\t* Wrong password! *");
                if (!retryOrGoBack(scn)) return false; // Go back to main menu
            }
        }
    }

    private static boolean retryOrGoBack(Scanner scn) {
        int retryOption = 0;
        while (retryOption != 1 && retryOption != 2) {
            System.out.print("Do you want to try again or go back? (1: Try again, 2: Go back): ");
            if (scn.hasNextInt()) {
                retryOption = scn.nextInt();
                if (retryOption == 2) return false; // Go back
            } else {
                System.out.println("Invalid input. Please enter 1 or 2.");
                scn.next(); // Clear invalid input
            }
        }
        return true; // Retry
    }

    private static void displayAndEditData(ParentManager parent) throws MessagingException {
        List<Integer> childIds = DatabaseManager.giveParentIdGetStuId(parent.getId());

        if (!childIds.isEmpty()) {
            System.out.println("your Data in the Database: ");
            System.out.println(parent.toString());
            System.out.println("Your child details:");
            for (int childId : childIds) {
                String[] childData = DatabaseManager.getStuData(childId);
                System.out.printf("ID: %d, Name: %s, Age: %s, Address: %s, School: %s, Teacher Number: %s, Status: %s%n",
                        childId, childData[0], childData[1], childData[2], childData[3], childData[4], childData[5]);
            }


            Scanner scanner = new Scanner(System.in);
            while (true) {
                System.out.print("1. Edit Any Student Details\n2. Edit Parent Details\n3. Exit\nEnter Your Choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline

                if (choice == 1) {
                    editStudentDetails(scanner, childIds,parent);
                } else if (choice == 2) {
                    editParentDetails(scanner, parent);
                } else if (choice == 3) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter 1, 2, or 3.");
                }
            }
        } else {
            System.out.println("No child details found.");
        }
    }

    public static void editStudentDetails(Scanner scanner, List<Integer> validIds,ParentManager parent) {
        int id;
        while (true) {
            System.out.print("Enter Student ID to update: ");
            id = scanner.nextInt();
            scanner.nextLine(); // Consume newline
            if (validIds.contains(id)) {
                break;
            } else {
                System.out.println("Invalid Student ID. Please enter one of the displayed IDs.");
            }
        }

        // Display specific student info
        String[] studentData = DatabaseManager.getStuData(id);
        System.out.printf("Current details - ID: %d, Name: %s, Age: %s, Address: %s, School: %s, Teacher Number: %s, Status: %s%n",
                id, studentData[0], studentData[1], studentData[2], studentData[3], studentData[4], studentData[5]);

        boolean continueEditing = true;
        while (continueEditing) {
            System.out.println("Select the detail to update:");
            System.out.println("1. Name");
            System.out.println("2. Age");
            System.out.println("3. Address");
            System.out.println("4. School");
            System.out.println("5. Teacher Number");
            System.out.println("6. Delete Student account");
            System.out.println("7. Done");
            System.out.print("Enter option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1: {
                    System.out.print("Enter New Student Name: ");
                    String name = scanner.nextLine();
                    DatabaseManager.updateStuField(id, "name", name);
                    break;
                }
                case 2: {
                    int age = 0;
                    while (true) {
                        System.out.print("Enter New Age: ");
                        if (scanner.hasNextInt()) {
                            age = scanner.nextInt();
                            scanner.nextLine(); // Consume newline
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter a valid age.");
                            scanner.next(); // Clear the invalid input
                        }
                    }
                    DatabaseManager.updateStuField(id, "age", age);
                    break;
                }
                case 3: {
                    System.out.print("Enter New Address: ");
                    String address = scanner.nextLine();
                    DatabaseManager.updateStuField(id, "address", address);
                    break;
                }

                case 4: {
                    System.out.print("Enter New School: ");
                    String school = scanner.nextLine();
                    DatabaseManager.updateStuField(id, "school", school);
                    break;
                }
                case 5: {
                    String teacherNum = "";
                    while (true) {
                        System.out.print("Enter New Teacher Number (10 digits): ");
                        teacherNum = scanner.next();
                        if (teacherNum.matches("\\d{10}")) {
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter a valid 10-digit phone number.");
                        }
                    }
                    DatabaseManager.updateStuField(id, "teacherNum", Integer.parseInt(teacherNum));
                    break;
                }
                case 6:{
                    removeStudent(scanner, parent);

                    return;
                }
                case 7: {
                    continueEditing = false;
                    break;
                }
                default: {
                    System.out.println("Invalid option selected. Please enter a number between 1 and 6.");
                }
            }

            // Display updated student details
            String[] updatedChildData = DatabaseManager.getStuData(id);
            System.out.printf("Updated details - ID: %d, Name: %s, Age: %s, Address: %s, School: %s, Teacher Number: %s, Status: %s%n",
                    id, updatedChildData[0], updatedChildData[1], updatedChildData[2], updatedChildData[3], updatedChildData[4], updatedChildData[5]);
        }

        System.out.println("Student details updated in the database.");
    }

    private static void editParentDetails(Scanner scanner, ParentManager parent) throws MessagingException {
        boolean continueEditing = true;

        while (continueEditing) {
            System.out.println("Information in the database : " + parent.toString());
            System.out.println("Select the detail to update:");
            System.out.println("1. Name\n2. Email\n3. Phone\n4. Add Student\n5. Remove Parent Account\n6. Done");
            System.out.print("Enter option: ");
            int option = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (option) {
                case 1: {
                    System.out.print("Enter New Parent Name: ");
                    parent.setName(scanner.nextLine());
                    break;
                }

                case 2: {
                    System.out.print("Enter New Email: ");
                    parent.setEmail(scanner.nextLine());

                    break;
                }
                case 3: {
                    String phone;
                    while (true) {
                        System.out.print("Enter New Phone Number (10 digits): ");
                        phone = scanner.next();
                        if (phone.matches("\\d{10}")) break;
                        System.out.println("Invalid input. Please enter a valid 10-digit phone number.");
                    }
                    parent.setPhone(phone);
                    break;
                }
                case 4: {
                    addStudent(scanner, parent);
                    break;
                }
                case 5: {
                   DatabaseManager.deleteParentData(parent.getId());
                    System.out.println("Please press any key to continue to main menu.");
                    login();
                    break;
                }
                case 6: {
                    continueEditing = false;
                    break;
                }
                default: {
                    System.out.println("Invalid option. Please try again.");
                }
            }
        }
    }

    private static void addStudent(Scanner scanner, ParentManager parent) {
        System.out.print("Enter Student Name: ");
        String stuName = scanner.nextLine();

        System.out.print("Enter Student Age: ");
        int stuAge = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter Student Address: ");
        String stuAddress = scanner.nextLine();

        System.out.print("Enter Student School: ");
        String stuSchool = scanner.nextLine();

        System.out.print("Enter Teacher Number (10 digits): ");
        String teacherNum = scanner.next();

        DatabaseManager.insertStuData(stuName, stuAge, stuAddress, stuSchool, teacherNum, "active", parent.getId());
        parent.setNumberOfStudents(parent.getNumberOfStudents() + 1);
    }

    private static void removeStudent(Scanner scanner, ParentManager parent) {
        System.out.println("This Cannot Be Undone, Are you Sure?(1-yes,2-no)");
        if (scanner.nextInt() == 1) {
            List<Integer> childIds = DatabaseManager.giveParentIdGetStuId(parent.getId());

            if (!childIds.isEmpty()) {
                System.out.println("Your child details:");
                for (int childId : childIds) {
                    String[] childData = DatabaseManager.getStuData(childId);
                    System.out.printf("ID: %d, Name: %s%n", childId, childData[0]);
                }
                System.out.print("Enter Student ID to remove: ");
                int stuId = scanner.nextInt();
                DatabaseManager.deleteStuData(stuId);
                parent.setNumberOfStudents(parent.getNumberOfStudents() - 1);
            } else {
                System.out.println("No child details found.");
            }
        } else {
            return;
        }
    }

    private static void DriverLogin(Scanner scn) {
        System.out.println("\n___________________________________Driver Login____________________________________\n");

        while (true) {
            String username=null;
            while(true){
            System.out.print("Enter username: ");
            username = scn.next();
            if(DatabaseManager.isDriverInDb(username)){
               break;
            }

            }
            int userId = DatabaseManager.giveDriverGetId(username);
            if (userId > 0) {
                if (processDriverPassword(scn, username, userId)) {
                    return; // Login successful
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
                return false; // Exit on failure to retrieve password
            }

            if (storedPassword.equals(inputPassword)) {
                System.out.println("Checking...");
                Driver driver = DatabaseManager.getDriverObject(id);
                if (driver.getId() != 0) {
                    System.out.println("\t*** Hello! " + driver.getName() + ", You are logged in! ***");
                    loginStatus = true;
                    System.out.println("Your details:\n" + driver.toString());
                    ChooseWhatToDoDriver(scn, driver);

                    // Perform actions for a logged-in driver
                    return true; // Login successful
                } else {
                    System.out.println("\t* Driver details not found. Please contact support. *");
                    return false;
                }
            } else {
                System.out.println("\t* Wrong password! *");
                if (!retryOrGoBack(scn)) return false; // Go back to main menu
            }
        }
    }


    private static void driverDataUpdate(Scanner scn, Driver driver) {
        boolean continueUpdating = true;
        while (continueUpdating) {
            System.out.println("\nCurrent Driver Details:");
            System.out.println(driver.toString());
            System.out.println("\nSelect the field to update:");
            System.out.println("1. Name");
            System.out.println("2. Password");
            System.out.println("3. Email");
            System.out.println("4. Phone");
            System.out.println("5. Van Number");
            System.out.println("6. Exit");

            System.out.print("Enter your choice: ");
            int choice = -1;

            // Loop to ensure valid integer input
            while (true) {
                try {
                    choice = scn.nextInt();
                    scn.nextLine(); // Consume the newline character
                    break; // Exit the loop if the input is valid
                } catch (java.util.InputMismatchException e) {
                    System.out.println("Invalid input. Please enter a valid integer.");
                    scn.nextLine(); // Clear the buffer
                }
            }

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String newName = scn.nextLine();
                    driver.setName(newName);
                    System.out.println("Name updated successfully!");
                    break;

                case 2:
                    System.out.print("Enter new password: ");
                    String newPassword = scn.nextLine();
                    driver.setPassword(newPassword);
                    System.out.println("Password updated successfully!");
                    break;

                case 3:
                    System.out.print("Enter new email: ");
                    String newEmail = scn.nextLine();
                    driver.setEmail(newEmail);
                    System.out.println("Email updated successfully!");
                    break;

                case 4:
                    System.out.print("Enter new phone number: ");
                    String newPhone = scn.nextLine();
                    driver.setPhone(newPhone);
                    System.out.println("Phone number updated successfully!");
                    break;

                case 5:
                    System.out.print("Enter new van number: ");
                    String newVanNumber = scn.nextLine();
                    driver.setVanNumber(newVanNumber);
                    System.out.println("Van number updated successfully!");
                    break;

                case 6:
                    continueUpdating = false;
                    System.out.println("Exiting update menu.");
                    break;

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }
        }
    }

    private static void ChooseWhatToDoDriver (Scanner scn,Driver driver){
        boolean set = true;
        while (set) {
            System.out.println("\nChoose from:");
            System.out.println("1. Update Account Details");
            System.out.println("2. Change Student Status");
            System.out.println("3. Exit");

            System.out.print("Enter your choice: ");
            int choice = scn.nextInt();
            scn.nextLine(); // Consume newline

            switch (choice) {
                case 1:
                    driverDataUpdate(scn, driver);
                    break;

                case 2:
                    System.out.println("------------------------Student Status Update section----------------------");
                    StudentManager.sortFromSchool(scn);
                    break;

                case 3:
                    set = false;
                    System.out.println("Exiting the menu. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please select a valid option.");
                    break;
            }

        }

    }

}


