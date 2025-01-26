
import jakarta.mail.MessagingException;

import java.util.Scanner;
import java.util.ArrayList;

class CreateNewUser {
    public static void CreateUser() throws MessagingException {
        boolean match = false;
        Scanner scn = new Scanner(System.in);

        System.out.println("\n________________________Create a new user _______________________\n");

        System.out.println("Select your role: \n1- Parent \n2- Driver");
        int role = scn.nextInt();
        scn.nextLine();

        if (role == 1) {
            System.out.print("Enter Name: ");
            String name = scn.nextLine();

            System.out.print("Enter e-mail: ");
            String email = scn.nextLine();
            while (email.contains(" ")) {
                System.out.println("The data you entered contains spaces. Please enter valid data.");
                email = scn.nextLine();
            }

            String phone ;
            while (true) {
                System.out.print("Enter phone number (10 digits): ");
                phone = scn.nextLine();
                if (phone.matches("\\d{10}")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid 10-digit phone number.");
                }
            }

            System.out.print("Enter Username: ");
            String username = scn.nextLine();
            while (username.contains(" ")) {
                System.out.println("The data you entered contains spaces. Please enter valid data.");
                username = scn.nextLine();
            }

            int UserID = DatabaseManager.giveUserGetId(username);
            while (UserID > 0) {
                System.out.println("\n\t* Username already taken! *\n");
                System.out.print("Enter another Username: ");
                username = scn.nextLine();
                while (username.contains(" ")) {
                    System.out.println("The data you entered contains spaces. Please enter valid data.");
                    username = scn.nextLine();
                }
                UserID = DatabaseManager.giveUserGetId(username);
            }

            System.out.print("Enter Password: ");
            String password = scn.nextLine();
            while (password.contains(" ")) {
                System.out.println("The data you entered contains spaces. Please enter valid data.");
                password = scn.nextLine();
            }

            System.out.print("Enter Password again: ");
            String cpassword = scn.nextLine();
            while (cpassword.contains(" ")) {
                System.out.println("The data you entered contains spaces. Please enter valid data.");
                cpassword = scn.nextLine();
            }

            if (password.equals(cpassword)) {
                match = true;
            } else {
                while (!match) {
                    System.out.println("\t* Passwords do not match, try again! *\n");
                    System.out.print("Enter Password: ");
                    password = scn.nextLine();
                    while (password.contains(" ")) {
                        System.out.println("The data you entered contains spaces. Please enter valid data.");
                        password = scn.nextLine();
                    }
                    System.out.print("Enter Password again: ");
                    cpassword = scn.nextLine();
                    while (cpassword.contains(" ")) {
                        System.out.println("The data you entered contains spaces. Please enter valid data.");
                        cpassword = scn.nextLine();
                    }
                    if (password.equals(cpassword)) {
                        match = true;
                    }
                }
            }

            System.out.print("Enter Number of students: ");
            int NoStu;
            while (true) {
                if (scn.hasNextInt()) {
                    NoStu = scn.nextInt();
                    scn.nextLine();
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid number of students.");
                    scn.next();
                }
            }

            if (match) {
                int ID = DatabaseManager.insertParentData(name, username, password, email, phone, NoStu);
                if (ID > 0) {
                    System.out.println("\n\t*** User created successfully ***\n");
                    EMAIL(email);
                }
            }

            if (NoStu > 0) {
                System.out.println("\n_________________________ Let's add student details _________________________\n");
                int parentID = DatabaseManager.giveUserGetId(username);
                String status = "dropped";
                ArrayList<Integer> arrList = new ArrayList<>();

                for (int i = 0; i < NoStu; i++) {
                    System.out.println("\nStudent " + (i + 1) + " details:\n");
                    System.out.print("Enter Name: ");
                    String stuName = scn.nextLine();

                    int stuAge ;
                    while (true) {
                        System.out.print("Enter Age: ");
                        if (scn.hasNextInt()) {
                            stuAge = scn.nextInt();
                            scn.nextLine();
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter a valid age.");
                            scn.next();
                        }
                    }

                    System.out.print("Enter Address: ");
                    String stuAddress = scn.nextLine();

                    System.out.print("Enter School: ");
                    String stuSchool = scn.nextLine();

                    String stuTphone ;
                    while (true) {
                        System.out.print("Enter Teacher's phone number (10 digits): ");
                        stuTphone = scn.nextLine();
                        if (stuTphone.matches("\\d{10}")) {
                            break;
                        } else {
                            System.out.println("Invalid input. Please enter a valid 10-digit phone number.");
                        }
                    }

                    int stuID = DatabaseManager.insertStuData(stuName, stuAge, stuAddress, stuSchool, stuTphone, status, parentID);
                    arrList.add(stuID);
                    if (stuID > 0) {
                        System.out.println("\n\t* Added student " + (i + 1) + " successfully *\n");
                    }
                }

                System.out.println("\t*** Successfully added student details ***\n");
                for (int i = 0; i < arrList.size(); i++) {
                    System.out.println("Student " + (i + 1) + " ID: " + arrList.get(i));
                }
                System.out.println("\n");
            } else {
                System.out.println("\n\t* No students *\n");
            }

        } else if (role == 2) {
            int status = DatabaseManager.getDriverRowCount();
            if (status == 1) {
                System.out.println("New driver accounts cannot be created because the system allows only one driver account, and an account already exists.");
                LoginManager.login();
                return;
            } else if (status > 1) {
                System.out.println("Contact Technical Support. Going back.");
                LoginManager.login();
                return;
            }

            System.out.print("Enter Name: ");
            String name = scn.nextLine();

            System.out.print("Enter e-mail: ");
            String email = scn.nextLine();
            while (email.contains(" ")) {
                System.out.println("The data you entered contains spaces. Please enter valid data.");
                email = scn.nextLine();
            }

            String phone = "";
            while (true) {
                System.out.print("Enter phone number (10 digits): ");
                phone = scn.nextLine();
                if (phone.matches("\\d{10}")) {
                    break;
                } else {
                    System.out.println("Invalid input. Please enter a valid 10-digit phone number.");
                }
            }

            System.out.print("Enter Username: ");
            String username = scn.nextLine();
            while (username.contains(" ")) {
                System.out.println("The data you entered contains spaces. Please enter valid data.");
                username = scn.nextLine();
            }

            int UserID = DatabaseManager.giveUserGetId(username);
            while (UserID > 0) {
                System.out.println("\n\t* Username already taken! *\n");
                System.out.print("Enter another Username: ");
                username = scn.nextLine();
                while (username.contains(" ")) {
                    System.out.println("The data you entered contains spaces. Please enter valid data.");
                    username = scn.nextLine();
                }
                UserID = DatabaseManager.giveUserGetId(username);
            }

            System.out.print("Enter Password: ");
            String password = scn.nextLine();
            while (password.contains(" ")) {
                System.out.println("The data you entered contains spaces. Please enter valid data.");
                password = scn.nextLine();
            }

            System.out.print("Enter Password again: ");
            String cpassword = scn.nextLine();
            while (cpassword.contains(" ")) {
                System.out.println("The data you entered contains spaces. Please enter valid data.");
                cpassword = scn.nextLine();
            }

            if (!password.equals(cpassword)) {
                System.out.println("\t* Passwords do not match. Restarting process. *\n");
                return;
            }

            System.out.print("Enter Van Number: ");
            String VanNumber = scn.nextLine();
            if (VanNumber.isEmpty()) {
                System.out.println("Van number cannot be empty!");
                return;
            }

            int ID = DatabaseManager.insertDriverData(name, username, password, email, phone, VanNumber);
            if (ID > 0) {
                System.out.println("\n\t*** Driver added successfully ***\n");
            } else {
                System.out.println("Failed to add driver. Please try again.");
            }

            LoginManager.login();
        } else {
            System.out.println("Data not entered! Please try again.");
            LoginManager.login();
        }

    }


    private static void EMAIL(String email) {
        String subject = "Welcome to Safe School Track - Your Account is Ready!";
        String body = "<html><body>"
                + "<h2 style='color:#4CAF50;'>Welcome to Safe School Track</h2>"
                + "<p style='font-size:16px;'>Your account was created successfully.</p>"
                + "<p style='font-size:16px;'>Thank you for joining the Safe School Track system!</p>"
                + "<p style='color:#888;'>If you have any questions, feel free to contact us.</p>"
                + "<footer style='font-size:12px;color:#aaa;'>Safe School Track Team</footer>"
                + "</body></html>";
        try {
            NotificationManager.sendMail(email,subject,body);
        } catch (MessagingException e) {
            System.err.println("\nFailed to send the email.\n");
            e.printStackTrace();
        }
    }

}