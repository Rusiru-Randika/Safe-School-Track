class Driver extends User {

        private String vanNumber;

        // Constructor
        public Driver(int id, String name, String username, String password, String email, String phone, String vanNumber) {
            super(id, name, username, password, email, phone);
            this.vanNumber = vanNumber;
        }
        public Driver(){
            super(0, "", "", "", "", "");  // Default values for parent field
            this.vanNumber = null;
        }

        @Override
        public void setName(String name) {
            this.name = name;
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
            }
        }

        @Override
        public void setPassword(String password) {
            this.password = password;
            DatabaseManager.updateDriverField(id,"pwd",password);

        }

        @Override
        public void setEmail(String email) {
            this.email = email;
            DatabaseManager.updateDriverField(id,"email",email);
        }

        @Override
        public void setPhone(String phone) {
            this.phone = phone;
            DatabaseManager.updateDriverField(id,"phone",phone);
        }

        public void setVanNumber(String vanNumber) {
            this.vanNumber = vanNumber;
            DatabaseManager.updateDriverField(id,"vanNo",vanNumber);
        }
    public String toString() {

        return String.format("| Id: %-5d | Name:%-15s | Username: %-15s | Email: %-20s | Phone: %-15s | Van Number: %-15s |",
                id, name, username, email, phone, vanNumber);
    }



}