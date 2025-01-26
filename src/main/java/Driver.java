import java.util.Scanner;

class Driver extends User {

        private String vanNumber;  // Driver's Van Number

        // Constructor
        public Driver(int id, String name, String username, String password, String email, String phone, String vanNumber) {
            super(id, name, username, password, email, phone); // Initialize User class fields
            this.vanNumber = vanNumber; // Initialize Driver class specific field
        }
        public Driver(){
            super(0, "", "", "", "", "");  // Default values for parent fields
            this.vanNumber = null;
        }

        @Override
        public void setName(String name) {
            this.name = name; // Set the name field in User class
            DatabaseManager.updateDriverField(id,"name",name);
        }

        @Override
        public void setUsername(String username) {
               if(!(DatabaseManager.isUserInDb(username))){
                DatabaseManager.updateDriverField(id,"username",username);
                this.username = username;
            }
            else{
                System.out.println("Already taken username");
            };
        }

        @Override
        public void setPassword(String password) {
            this.password = password;
            DatabaseManager.updateDriverField(id,"pwd",password);

        }

        @Override
        public void setEmail(String email) {
            this.email = email; // Set the email field in User class
            DatabaseManager.updateDriverField(id,"email",email);
        }

        @Override
        public void setPhone(String phone) {
            this.phone = phone; // Set the phone field in User class
            DatabaseManager.updateDriverField(id,"phone",phone);
        }

        // Getter and Setter for vanNumber
        public String getVanNumber() {
            return vanNumber; // Return the van number
        }

        public void setVanNumber(String vanNumber) {
            this.vanNumber = vanNumber;
            DatabaseManager.updateDriverField(id,"vanNo",vanNumber);// Set the van number
        }
    public String toString() {
        // Return formatted string to display as a table row including number of students
        return String.format("| Id: %-5d | Name:%-15s | Username: %-15s | Email: %-20s | Phone: %-15s | Van Number: %-15s |",
                id, name, username, email, phone, vanNumber);
    }



}