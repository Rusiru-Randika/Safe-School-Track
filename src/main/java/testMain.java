
import java.util.List;

public class testMain {
    public static void main(String[] args) {
        // Step 1: Connect to the database
        DatabaseManager.connectToDatabase();

        // Step 2: Add at least 10 data rows
        System.out.println("Adding parent data...");
        for (int i = 1; i <= 10; i++) {
            String name = "Parent" + i;
            String username = "user" + i;
            String password = "password" + i;
            String email = "parent" + i + "@example.com";
            int phone = 1000000 + i;
            int noOfStu = i % 3 + 1; // Randomize number of students between 1 and 3

            int id = DatabaseManager.insertParentData(name, username, password, email, phone, noOfStu);
            if (id != -1) {
                System.out.println("Inserted Parent with ID: " + id);
            } else {
                System.out.println("Failed to insert Parent data for: " + name);
            }
        }

        // Step 3: Print all parent data
        System.out.println("\nPrinting all parent data:");
        DatabaseManager.printParentTableData();

        // Step 4: Update a field for a specific parent
        System.out.println("\nUpdating Parent 1's email...");
        boolean updateSuccess = DatabaseManager.updateParentField(1, "email", "newemail@example.com");
        if (updateSuccess) {
            System.out.println("Email updated successfully.");
        } else {
            System.out.println("Failed to update email.");
        }

        // Step 5: Retrieve a specific parent's data
        System.out.println("\nRetrieving data for Parent 1:");
        String[] parentData = DatabaseManager.getParentData(1);
        if (parentData[0] != null) {
            System.out.printf("Name: %s, Username: %s, Password: %s, Email: %s, Phone: %s, No of Students: %s%n",
                    parentData[0], parentData[1], parentData[2], parentData[3], parentData[4], parentData[5]);
        } else {
            System.out.println("No data found for Parent 1.");
        }

        // Step 6: Check if a username exists
        System.out.println("\nChecking if username 'user5' exists...");
        boolean userExists = DatabaseManager.isUserInDb("user5");
        System.out.println("Username 'user5' exists: " + userExists);

        // Step 7: Retrieve password by username
        System.out.println("\nRetrieving password for username 'user5'...");
        String password = DatabaseManager.giveUserGetPassword("user5");
        System.out.println("Password for 'user5': " + password);

        // Step 8: Retrieve ID by username, phone, or email
        System.out.println("\nRetrieving ID for username 'user5'...");
        int idByUsername = DatabaseManager.giveUserGetId("user5");
        System.out.println("ID for 'user5': " + idByUsername);

        System.out.println("\nRetrieving ID for phone '1000005'...");
        int idByPhone = DatabaseManager.givePhoneGetId(1000005);
        System.out.println("ID for phone '1000005': " + idByPhone);

        System.out.println("\nRetrieving ID for email 'parent5@example.com'...");
        int idByEmail = DatabaseManager.giveEmailGetId("parent5@example.com");
        System.out.println("ID for email 'parent5@example.com': " + idByEmail);

        // Step 9: Delete a parent record
        System.out.println("\nDeleting Parent 10...");
        DatabaseManager.deleteParentData(10);

        // Step 10: Print all parent data again to verify deletion
        System.out.println("\nPrinting all parent data after deletion:");
        DatabaseManager.printParentTableData();


        // Insert sample data into the Student table
        int parentId1 = 1; // Example parent ID
        int parentId2 = 2; // Example parent ID

        // Inserting 10 students
        int a=DatabaseManager.insertStuData("Alice", "10", "123 Main St", "Greenwood High", 101, "Active", parentId1);
        int b=DatabaseManager.insertStuData("Bob", "11", "456 Elm St", "Greenwood High", 102, "Active", parentId1);
        int c=DatabaseManager.insertStuData("Charlie", "12", "789 Maple St", "Riverdale High", 201, "Inactive", parentId1);
        int d=DatabaseManager.insertStuData("Diana", "13", "321 Oak St", "Greenwood High", 103, "Active", parentId2);
        int e=DatabaseManager.insertStuData("Eve", "14", "654 Pine St", "Hilltop High", 301, "Active", parentId2);
        int aa=DatabaseManager.insertStuData("Frank", "10", "987 Cedar St", "Riverdale High", 202, "Inactive", parentId2);
        int bb= DatabaseManager.insertStuData("Grace", "11", "159 Birch St", "Hilltop High", 302, "Active", parentId1);
        int cc=DatabaseManager.insertStuData("Hank", "12", "753 Willow St", "Greenwood High", 104, "Inactive", parentId2);
        int dd=DatabaseManager.insertStuData("Ivy", "13", "951 Spruce St", "Riverdale High", 203, "Active", parentId1);
        int ee=DatabaseManager.insertStuData("Jack", "14", "852 Fir St", "Hilltop High", 303, "Active", parentId2);

        // Print all student data
        System.out.println("\n--- Printing all student data ---");
        DatabaseManager.printStuTableData();

        // Update student data
        System.out.println("\n--- Updating student data ---");
        boolean s= DatabaseManager.updateStuField(1, "name", "Alice Johnson"); // Update name of student with ID 1
        boolean q= DatabaseManager.updateStuField(2, "age", "12");             // Update age of student with ID 2
        boolean r=DatabaseManager.updateStuField(3, "address", "101 New St"); // Update address of student with ID 3

        // Print updated data
        System.out.println("\n--- Printing updated student data ---");
        DatabaseManager.printStuTableData();

        // Retrieve data of a specific student
        System.out.println("\n--- Retrieving data of student with ID 1 ---");
        String[] studentData = DatabaseManager.getStuData(1);
        System.out.printf("Name: %s, Age: %s, Address: %s, School: %s, Teacher Number: %s, Status: %s, Parent ID: %s\n",
                studentData[0], studentData[1], studentData[2], studentData[3], studentData[4], studentData[5], studentData[6]);

        // Retrieve student IDs by school
        System.out.println("\n--- Retrieving student IDs by school (Greenwood High) ---");
        List<Integer> greenwoodStudents = DatabaseManager.giveSchoolGetStuList("Greenwood High");
        System.out.println("Student IDs in Greenwood High: " + greenwoodStudents);

        // Retrieve student IDs by parent ID
        System.out.println("\n--- Retrieving student IDs by parent ID (Parent ID: 1) ---");
        List<Integer> parentStudents = DatabaseManager.giveParentIdGetStuId(1);
        System.out.println("Student IDs for Parent ID 1: " + parentStudents);

        // Delete a student record
        System.out.println("\n--- Deleting student with ID 10 ---");
        DatabaseManager.deleteStuData(10);

        // Print all data after deletion
        System.out.println("\n--- Printing all student data after deletion ---");
        DatabaseManager.printStuTableData();



// Step 11: Close the database connection
        DatabaseManager.closeConnection();
    }
}
