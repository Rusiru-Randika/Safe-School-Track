import java.util.Scanner;

public class Student_Manager {
    String Student_Id;
    String Student_Name;
    int Student_Age;
    String Student_Address;
    String Student_School;
    String Student_Teacher_Number;

    public Student_Manager(String Student_Id, String Student_Name, int Student_Age, String Student_Address, String Student_School, String Student_Teacher_Number) {
        this.Student_Id = Student_Id;
        this.Student_Name = Student_Name;
        this.Student_Age = Student_Age;
        this.Student_Address = Student_Address;
        this.Student_School = Student_School;
        this.Student_Teacher_Number = Student_Teacher_Number;
    }

    public void Display_Info() {
        System.out.println("Student Data from Database:");
        DatabaseManager.printStuTableData();
    }

    public void editStudentDetails(Scanner scanner) {
        System.out.print("Enter Student ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter New Student Name: ");
        String name = scanner.nextLine();

        System.out.print("Enter New School: ");
        String school = scanner.nextLine();

        System.out.print("Enter New Grade: ");
        String grade = scanner.nextLine();

        System.out.print("Enter New Teacher Number: ");
        int teacherNum = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter New Address: ");
        String address = scanner.nextLine();

        System.out.print("Enter New Email: ");
        String email = scanner.nextLine();

//        DatabaseManager.updateStudent(id, name, school, grade, teacherNum, address, email);
        System.out.println("Student details updated in the database.");
    }

    public void addStudent(String name, String school, String grade, int teacherNum, String address, String email) {
//        DatabaseManager.insertStudentData(name, school, grade, teacherNum, address, email);
    }
}