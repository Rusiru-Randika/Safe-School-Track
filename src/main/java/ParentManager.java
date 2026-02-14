class ParentManager extends User {
    private int numberOfStudents;

    public ParentManager(int id, String name, String username, String password, String email, String phone, int numberOfStudents) {
        super(id, name, username, password, email, phone);
        this.numberOfStudents = numberOfStudents;
    }

    public ParentManager() {
        super(0, "", "", "", "", "");
        this.numberOfStudents = 0;
    }

    @Override
    public void setName(String name) {
        this.name = name;
        DatabaseManager.updateParentField(id, "name", name);
    }

    @Override
    public void setUsername(String username) {
        if (!DatabaseManager.isUserInDb(username)) {
            DatabaseManager.updateParentField(id, "username", username);
            this.username = username;
        } else {
            System.out.println("Already taken username");
        }
    }

    @Override
    public void setPassword(String password) {
        this.password = password;
        DatabaseManager.updateParentField(id, "pwd", password);
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

    public int getNumberOfStudents() {
        return numberOfStudents;
    }

    public void setNumberOfStudents(int numberOfStudents) {
        this.numberOfStudents = numberOfStudents;
        DatabaseManager.updateParentField(id, "NOS", numberOfStudents);
    }

    @Override
    public String toString() {
        return String.format("| Id: %-5d | Name: %-15s | Username: %-15s | Email: %-20s | Phone: %-15s | Students: %-5d |",
                id, name, username, email, phone, numberOfStudents);
    }
}
