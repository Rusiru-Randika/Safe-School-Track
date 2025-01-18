import java.util.Scanner;
import java.util.ArrayList;

public class CreateNewUser {
    public static void CreateUser() {
        Boolean match = false;
        Scanner scn = new Scanner(System.in);
        System.out.println("\n________________________Create a new user (use one word)_______________________\n");

        System.out.print("Enter Name : ");
        String name = scn.next();
        System.out.print("Enter e-mail : ");
        String email = scn.next();
        System.out.print("Enter phone number : ");
        int phone = scn.nextInt();
        System.out.print("Enter Number of students : ");
        int NoStu = scn.nextInt();
        System.out.print("Enter Username : ");
        String username = scn.next();
        int UserID = DatabaseManager.giveUserGetId(username);
        while (UserID > 0) {
            System.out.println("\n\t* Already taken!* \n");
            System.out.print("Enter another Username : ");
            username = scn.next();
            UserID = DatabaseManager.giveUserGetId(username);
        }
        System.out.print("Enter Password : ");
        String password = scn.next();
        System.out.print("Enter Password again : ");
        String cpassword = scn.next();
        if(password.equals(cpassword)) {
            match = true;
        }
        else {
            while(!match) {
                System.out.println("\t* Passwords do not match , try again! *\n");
                System.out.print("Enter Password : ");
                password = scn.next();
                System.out.print("Enter Password again : ");
                cpassword = scn.next();
                if(password.equals(cpassword)) {
                    match = true;
                }
            }
        }
       if(match) {
           int ID = DatabaseManager.insertParentData(name,username,password,email,phone,NoStu);
           if(ID > 0) {
               System.out.println("\n\t*** User created successfully ***\n");
           }
       }
       if(NoStu>0) {
           System.out.println("\n_________________________Lets add student details_________________________\n");

           int parentID = DatabaseManager.giveUserGetId(username);
           String status = "dropped";

           ArrayList<Integer> arrList = new ArrayList<>();


           for (int i = 0; i < NoStu; i++) {
               System.out.println("\nStudent " + (i + 1) + " details (use one word) :\n");
               System.out.print("Enter Name : ");
               String stuName = scn.next();
               System.out.print("Enter Age : ");
               int stuAge = scn.nextInt();
               System.out.print("Enter Address : ");
               String stuAddress = scn.next();
               System.out.print("Enter School : ");
               String stuSchool = scn.next();
               System.out.print("Enter Teacher's phone number : ");
               int stuTphone = scn.nextInt();
               int stuID = DatabaseManager.insertStuData(stuName, stuAge, stuAddress, stuSchool, stuTphone, status, parentID);
               arrList.add(stuID);
               if (stuID > 0) {
                   System.out.println("\n\t* Added student " + (i + 1) + " successfully *\n");
               }
           }

           System.out.println("\t*** Successfully added student details ***\n");
           for (int i = 0; i < arrList.size(); i++) {
               System.out.println("Student " + (i + 1) + " ID : " + arrList.get(i));
           }
           System.out.println("\n");
       }
       else{
           System.out.println("\n\t* No students *\n");
       }

        LoginManager.login();
    }

}
