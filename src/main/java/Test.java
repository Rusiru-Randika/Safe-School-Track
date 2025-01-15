public class Test {
    public static void main(String[] args) {
        Database_Manager.connectToDatabase();

        // Add Parent Data
        Parent_Manager parent1 = new Parent_Manager("1", "Parent 1", "Abc1@xyz.com", 1234567891);
        parent1.addParent("Parent 1", "password123", 1234567891, 101, "Student A");

        // Add Student Data
        Student_Manager student1 = new Student_Manager("S001", "Student A", 16, "Address 1", "School 1", "0771234567");
        student1.addStudent("Student A", "School 1", "Grade 10", 123456, "Address 1", "studentA@school.com");

        // Display Parent Info
        parent1.Display_Info();

        // Display Student Info
        student1.Display_Info();

        // Close Database Connection
        Database_Manager.closeConnection();
    }
}
