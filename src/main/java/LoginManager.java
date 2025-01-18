import java.util.Scanner;

public class LoginManager {
    static boolean Login_status=false;
    public static boolean getLoginStatus() {
        return Login_status;
    }

    public static Boolean login() {
        System.out.println("\n________________________________Welcome to Login_________________________________\n");
        int option=0;
        while (option != 3) {
        System.out.println("Select an option \n 1) Login \n 2) Create new account \n 3) Exit ");
        System.out.print("\nSelected option : ");
        Scanner scn = new Scanner(System.in);
        option = scn.nextInt();
            if (option == 1) {
                System.out.println("\n______________________________________Login____________________________________\n");
                Boolean success=false;
                int Max_login_attempts=3;
                int Num_login_attempts=0;
                    System.out.print("Enter username : ");
                    String username = scn.next();
                    System.out.println("\tLoading...");
                    int UserID = DatabaseManager.giveUserGetId(username);
                if (UserID > 0) {
                    String password = DatabaseManager.giveUserGetPassword(username);
                    while (Num_login_attempts < Max_login_attempts) {
                        if (Num_login_attempts == (Max_login_attempts - 1)) {
                            System.out.println("You have only 1 attempt more...");
                        } else {
                            System.out.println("You have " + (Max_login_attempts - Num_login_attempts) + " attempts...");
                        }
                        System.out.print("Enter password : ");
                        String input_password = scn.next();
                        if (password.equals(input_password)) {
                            System.out.println("Checking...");
                            success = true;
                            String[] arr = DatabaseManager.getParentData(UserID);
                            System.out.println("\t*** Hello! " + arr[0] + ", You are logged in! ***" + "\nYour User ID is " + UserID + " \n");
                            Login_status = true;

                            String ans="";

                            while (ans != "Y" || ans != "N") {
                                System.out.print("\nDo you want to change password? (Y/N) : ");
                                ans = scn.next();
                                if (ans.equals("Y")) {
                                    System.out.print("\nEnter new password : ");
                                    String newPassword = scn.next();
                                    Boolean result = DatabaseManager.updateParentField(UserID, "pwd", newPassword);
                                    if (result) {
                                        System.out.println("*** Password changed successfully! ***");
                                    } else {
                                        System.out.println("*** Password change failed! ***");
                                    }
                                } else if (ans.equals("N")) {
                                    break;
                                }
                            }
                            return true;
                        }


                           else {
                                System.out.println("\t* Wrong password! *");
                                success = false;
                                if(Num_login_attempts==(Max_login_attempts-1)){
                                    System.out.println("\t* Please try again next time *\n");
                                    return false;
                                }
                                Num_login_attempts++;
                            }

                        }

break;
                    }
                    else {
                        System.out.println("* Invalid username entered *");
                        login();
                    }

            }
            else if (option == 2) {
                CreateNewUser.CreateUser();
                break;
            }
            else if(option == 3) {
                System.out.println("_______________________________See you again!_______________________________\n");
                break;
            }
            else {
                System.out.println("Enter a valid option\n");
            }
        }
return false;
    }

}
