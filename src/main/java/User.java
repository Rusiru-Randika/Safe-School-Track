abstract class User {
    protected int id;
    protected String name;
    protected String username;
    protected String password;
    protected String email;
    protected String phone;

    public User(int id, String name, String username, String password, String email, String phone) {
        this.id = id;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public abstract void setName(String name);

    public String getUsername() {
        return username;
    }

    public abstract void setUsername(String username);

    public String getPassword() {
        return password;
    }

    public abstract void setPassword(String password);

    public String getEmail() {
        return email;
    }

    public abstract void setEmail(String email);

    public String getPhone() {
        return phone;
    }

    public abstract void setPhone(String phone);

    @Override
    public String toString() {
        return String.format("| %-5d | %-15s | %-15s | %-20s | %-15s |", id, name, username, email, phone);
    }
}
