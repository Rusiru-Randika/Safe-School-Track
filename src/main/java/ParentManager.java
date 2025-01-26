import java.util.List;
import java.util.Scanner;
class ParentManager extends User {
    private int numberOfStudents; // Number of Students

    // Constructor
    public ParentManager(int id, String name, String username, String password, String email, String phone, int numberOfStudents) {
        super(id, name, username, password, email, phone);
        this.numberOfStudents = numberOfStudents;
    }
    public ParentManager() {
        super(0, "", "", "", "", "");  // Default values for parent fields
        this.numberOfStudents = 0;      // Default value for number of students
    }

    @Override
    public void setName(String name) {
        this.name = name;
       DatabaseManager.updateParentField(id, "name", name);
    }

    @Override
    public void setUsername(String username) {
        if(!(DatabaseManager.isUserInDb(username))){
           DatabaseManager.updateParentField(id,"username",username);
            this.username = username;
        }
        else{
            System.out.println("Already taken username");
        };
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
        DatabaseManager.updateParentField(id,"pwd",password);
    }

    @Override
    public void setEmail(String email) {
        this.email = email;
        DatabaseManager.updateParentField(id, "email", email);
    }

    @Override
    public void setPhone(String phone) {
        this.phone = phone;
        DatabaseManager.updateParentField(id, "phone", phone);
    }

    // Getter and Setter for numberOfStudents
    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
        DatabaseManager.updateParentField(id, "NOS", numberOfStudents);
    }
    public String toString() {
        // Return formatted string to display as a table row including number of students
        return String.format("| Id: %-5d | Name:%-15s | Username: %-15s | Email: %-20s | Phone: %-15s | Number Of Student: %-15d |\n\n",
                id, name, username, email, phone, numberOfStudents);
    }


}
