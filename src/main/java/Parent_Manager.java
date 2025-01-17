import java.util.Scanner;

public class Parent_Manager {
    String Parent_Id;
    String Parent_Name;
    String Parent_Email;
    int Parent_Phone;

    public Parent_Manager(String Parent_Id, String Parent_Name, String Parent_Email, int Parent_Phone) {
        this.Parent_Id = Parent_Id;
        this.Parent_Name = Parent_Name;
        this.Parent_Email = Parent_Email;
        this.Parent_Phone = Parent_Phone;
    }

    public void Display_Info() {
        System.out.println("Parent Data from Database:");
        DatabaseManager.printParentTableData();
    }

    public void Change_Parent_Details() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter Parent ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter New Parent Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter New Password: ");
        String pwd = scanner.nextLine();

        System.out.print("Enter New Phone Number: ");
        int phone = scanner.nextInt();

        System.out.print("Enter New Student ID: ");
        int stuId = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter New Student Name: ");
        String stuName = scanner.nextLine();

        DatabaseManager.updateParent(id, name, pwd, phone, stuId, stuName);
        System.out.println("Parent details updated in the database.");
    }

    public void addParent(String name, String pwd, int phone, int stuId, String stuName) {
        DatabaseManager.insertParentData(name, pwd, phone, stuId, stuName);
    }
}
