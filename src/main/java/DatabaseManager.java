import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:Database/Database.db";
    private static Connection connection;

    // Field name mappings to avoid repetition in update methods
    private static final Map<String, String> PARENT_FIELD_MAP = new HashMap<>();
    private static final Map<String, String> STUDENT_FIELD_MAP = new HashMap<>();
    private static final Map<String, String> DRIVER_FIELD_MAP = new HashMap<>();

    static {
        PARENT_FIELD_MAP.put("name", "Parent_Name");
        PARENT_FIELD_MAP.put("username", "USERNAME");
        PARENT_FIELD_MAP.put("pwd", "Parent_Password");
        PARENT_FIELD_MAP.put("email", "Parent_Email");
        PARENT_FIELD_MAP.put("phone", "Parent_Phone");
        PARENT_FIELD_MAP.put("NOS", "Parent_NOS");

        STUDENT_FIELD_MAP.put("name", "Student_Name");
        STUDENT_FIELD_MAP.put("age", "Student_Age");
        STUDENT_FIELD_MAP.put("address", "Student_Address");
        STUDENT_FIELD_MAP.put("school", "Student_School");
        STUDENT_FIELD_MAP.put("teacherNum", "Student_Teacher_Number");
        STUDENT_FIELD_MAP.put("Student_Status", "Student_Status");
        STUDENT_FIELD_MAP.put("parentId", "Parent_Id");

        DRIVER_FIELD_MAP.put("name", "Driver_Name");
        DRIVER_FIELD_MAP.put("username", "USERNAME");
        DRIVER_FIELD_MAP.put("pwd", "Driver_Password");
        DRIVER_FIELD_MAP.put("email", "Driver_Email");
        DRIVER_FIELD_MAP.put("phone", "Driver_Phone");
        DRIVER_FIELD_MAP.put("vanNo", "Driver_VanNo");
    }

    // =========================================================================
    // Connection Management
    // =========================================================================

    public static void connectToDatabase() {
        try {
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connected to database");

            try (Statement stmt = connection.createStatement()) {
                stmt.execute("PRAGMA foreign_keys = ON;");
                System.out.println("Foreign key support enabled.");
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            } else {
                System.out.println("Connection is already closed.");
            }
        } catch (Exception e) {
            System.out.println("Failed to close connection: " + e.getMessage());
        } finally {
            connection = null;
        }
    }

    // =========================================================================
    // Parent Table Operations
    // =========================================================================

    public static void createParentTable() {
        if (connection == null) {
            System.out.println("Connection is null. Cannot create table.");
            return;
        }
        String sql = """
                CREATE TABLE IF NOT EXISTS Parent (
                    Parent_Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    Parent_Name TEXT NOT NULL,
                    USERNAME TEXT NOT NULL,
                    Parent_Password TEXT NOT NULL,
                    Parent_Email TEXT NOT NULL,
                    Parent_Phone TEXT NOT NULL,
                    Parent_NOS INTEGER NOT NULL
                );
                """;
        executeUpdate(sql, "Parent");
    }

    public static int insertParentData(String name, String username, String pwd,
                                       String email, String phone, int noOfStu) {
        createParentTable();
        String sql = "INSERT INTO Parent (Parent_Name, USERNAME, Parent_Password, Parent_Email, Parent_Phone, Parent_NOS) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, pwd);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setInt(6, noOfStu);
            stmt.executeUpdate();
            return getGeneratedId(stmt, "Parent", name);
        } catch (Exception e) {
            System.out.println("Failed to insert data: " + e.getMessage());
            return -1;
        }
    }

    public static void printParentTableData() {
        String sql = "SELECT * FROM Parent;";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("Parent data in the database:");
            while (rs.next()) {
                System.out.printf("ID: %d, Name: %s, Username: %s, Password: %s, Email: %s, Phone: %s, No of Students: %d%n",
                        rs.getInt("Parent_Id"),
                        rs.getString("Parent_Name"),
                        rs.getString("USERNAME"),
                        rs.getString("Parent_Password"),
                        rs.getString("Parent_Email"),
                        rs.getString("Parent_Phone"),
                        rs.getInt("Parent_NOS"));
            }
        } catch (Exception e) {
            System.out.println("Failed to read data: " + e.getMessage());
        }
    }

    public static void updateParentField(int id, String fieldToUpdate, Object newValue) {
        // Check username uniqueness before updating
        if ("username".equals(fieldToUpdate) && isUserInDb(newValue.toString())) {
            System.out.println("New username already used");
            return;
        }

        String columnName = PARENT_FIELD_MAP.get(fieldToUpdate);
        if (columnName == null) {
            System.out.println("Invalid field to update: " + fieldToUpdate);
            return;
        }

        String sql = "UPDATE Parent SET " + columnName + " = ? WHERE Parent_Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if ("NOS".equals(fieldToUpdate)) {
                stmt.setInt(1, (Integer) newValue);
            } else {
                stmt.setString(1, newValue.toString());
            }
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println(fieldToUpdate + " updated for Parent with ID: " + id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void deleteParentData(int id) {
        String sql = "DELETE FROM Parent WHERE Parent_Id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Deleted Parent with ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to delete data: " + e.getMessage());
        }
    }

    public static ParentManager getParentObject(int id) {
        String sql = "SELECT * FROM Parent WHERE Parent_Id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new ParentManager(
                            id,
                            rs.getString("Parent_Name"),
                            rs.getString("USERNAME"),
                            rs.getString("Parent_Password"),
                            rs.getString("Parent_Email"),
                            rs.getString("Parent_Phone"),
                            rs.getInt("Parent_NOS")
                    );
                }
            }
            System.out.println("No data found for ID: " + id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new ParentManager();
    }

    public static boolean isUserInDb(String userName) {
        String sql = "SELECT 1 FROM Parent WHERE USERNAME = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
            System.out.println("No existing User in this UserName");
            return false;
        } catch (Exception e) {
            System.out.println("Error checking User Name: " + e.getMessage());
            return false;
        }
    }

    public static String giveUserGetPassword(String userName) {
        String sql = "SELECT Parent_Password FROM Parent WHERE USERNAME = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Parent_Password");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static int giveUserGetId(String userName) {
        String sql = "SELECT Parent_Id FROM Parent WHERE USERNAME = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Parent_Id");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return -1;
    }

    public static int givePhoneGetId(String phone) {
        String sql = "SELECT Parent_Id FROM Parent WHERE Parent_Phone = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Parent_Id");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    public static int giveEmailGetId(String email) {
        String sql = "SELECT Parent_Id FROM Parent WHERE Parent_Email = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, email);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Parent_Id");
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return 0;
    }

    // =========================================================================
    // Student Table Operations
    // =========================================================================

    private static void createStuTable() {
        if (connection == null) {
            System.out.println("Connection is null. Cannot create table.");
            return;
        }
        String sql = """
                CREATE TABLE IF NOT EXISTS Student (
                    Student_Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    Student_Name TEXT NOT NULL,
                    Student_Age INTEGER NOT NULL,
                    Student_Address TEXT NOT NULL,
                    Student_School TEXT NOT NULL,
                    Student_Teacher_Number TEXT NOT NULL,
                    Student_Status TEXT NOT NULL,
                    Parent_Id INTEGER NOT NULL,
                    FOREIGN KEY (Parent_Id) REFERENCES Parent (Parent_Id) ON DELETE CASCADE ON UPDATE CASCADE
                );
                """;
        executeUpdate(sql, "Student");
    }

    public static int insertStuData(String name, int age, String address,
                                    String school, String teacherNum, String stuStatus, int parentId) {
        createStuTable();
        String sql = "INSERT INTO Student (Student_Name, Student_Age, Student_Address, Student_School, Student_Teacher_Number, Student_Status, Parent_Id) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setInt(2, age);
            stmt.setString(3, address);
            stmt.setString(4, school);
            stmt.setString(5, teacherNum);
            stmt.setString(6, stuStatus);
            stmt.setInt(7, parentId);
            stmt.executeUpdate();
            return getGeneratedId(stmt, "Student", name);
        } catch (Exception e) {
            System.out.println("Failed to insert data: " + e.getMessage());
            return -1;
        }
    }

    public static void printStuTableData() {
        String sql = "SELECT * FROM Student;";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.printf("| %-10s | %-15s | %-5s | %-20s | %-15s | %-15s | %-55s | %-20s |%n",
                    "ID", "Name", "Age", "Address", "School", "TeacherNum", "Status", "ParentID");
            System.out.println(
                    "+------------+-----------------+-------+----------------------+-----------------+-----------------+-------------------------------------------------------+------------+");
            while (rs.next()) {
                System.out.printf("| %-10d | %-15s | %-5d | %-20s | %-15s | %-15s | %-55s | %-20d |%n",
                        rs.getInt("Student_Id"),
                        rs.getString("Student_Name"),
                        rs.getInt("Student_Age"),
                        rs.getString("Student_Address"),
                        rs.getString("Student_School"),
                        rs.getString("Student_Teacher_Number"),
                        rs.getString("Student_Status"),
                        rs.getInt("Parent_Id"));
            }
        } catch (Exception e) {
            System.out.println("Failed to read data: " + e.getMessage());
        }
    }

    public static boolean updateStuField(int id, String fieldToUpdate, Object newValue) {
        String columnName = STUDENT_FIELD_MAP.get(fieldToUpdate);
        if (columnName == null) {
            System.out.println("Invalid field to update: " + fieldToUpdate);
            return false;
        }

        String sql = "UPDATE Student SET " + columnName + " = ? WHERE Student_Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            if ("age".equals(fieldToUpdate) || "parentId".equals(fieldToUpdate)) {
                stmt.setInt(1, (Integer) newValue);
            } else {
                stmt.setString(1, newValue.toString());
            }
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println(fieldToUpdate + " updated for Student with ID: " + id);
            return true;
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return false;
        }
    }

    public static void deleteStuData(int id) {
        String sql = "DELETE FROM Student WHERE Student_Id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Deleted Student with ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to delete data: " + e.getMessage());
        }
    }

    public static ArrayList<String> getDistinctSchoolTypes() {
        ArrayList<String> schoolList = new ArrayList<>();
        String sql = "SELECT DISTINCT Student_School FROM Student;";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                schoolList.add(rs.getString("Student_School"));
            }
            System.out.println("School list accessed");
        } catch (Exception e) {
            System.out.println("Failed to retrieve school types: " + e.getMessage());
        }
        return schoolList;
    }

    public static String[] getStuData(int id) {
        String[] stuData = new String[7];
        String sql = "SELECT * FROM Student WHERE Student_Id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    stuData[0] = rs.getString("Student_Name");
                    stuData[1] = String.valueOf(rs.getInt("Student_Age"));
                    stuData[2] = rs.getString("Student_Address");
                    stuData[3] = rs.getString("Student_School");
                    stuData[4] = rs.getString("Student_Teacher_Number");
                    stuData[5] = rs.getString("Student_Status");
                    stuData[6] = String.valueOf(rs.getInt("Parent_Id"));
                } else {
                    System.out.println("No data found for ID: " + id);
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return stuData;
    }

    public static List<Integer> giveSchoolGetStuList(String school) {
        List<Integer> stuInSchool = new ArrayList<>();
        String sql = "SELECT Student_Id FROM Student WHERE Student_School = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, school);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stuInSchool.add(rs.getInt("Student_Id"));
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve student IDs: " + e.getMessage());
        }
        return stuInSchool;
    }

    public static List<Integer> giveParentIdGetStuId(int parentId) {
        List<Integer> stuOfParent = new ArrayList<>();
        String sql = "SELECT Student_Id FROM Student WHERE Parent_Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, parentId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    stuOfParent.add(rs.getInt("Student_Id"));
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to retrieve student IDs: " + e.getMessage());
        }
        return stuOfParent;
    }

    public static boolean isStudentIdValid(int id) {
        String sql = "SELECT 1 FROM Student WHERE Student_Id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("Error checking student ID: " + e.getMessage());
            return false;
        }
    }

    // =========================================================================
    // Driver Table Operations
    // =========================================================================

    public static void createDriverTable() {
        if (connection == null) {
            System.out.println("Connection is null. Cannot create table.");
            return;
        }
        String sql = """
                CREATE TABLE IF NOT EXISTS Driver (
                    Driver_Id INTEGER PRIMARY KEY AUTOINCREMENT,
                    Driver_Name TEXT NOT NULL,
                    USERNAME TEXT NOT NULL,
                    Driver_Password TEXT NOT NULL,
                    Driver_Email TEXT NOT NULL,
                    Driver_Phone TEXT NOT NULL,
                    Driver_VanNo TEXT NOT NULL
                );
                """;
        executeUpdate(sql, "Driver");
    }

    public static int insertDriverData(String name, String username, String pwd,
                                       String email, String phone, String vanNumber) {
        createDriverTable();
        String sql = "INSERT INTO Driver (Driver_Name, USERNAME, Driver_Password, Driver_Email, Driver_Phone, Driver_VanNo) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, name);
            stmt.setString(2, username);
            stmt.setString(3, pwd);
            stmt.setString(4, email);
            stmt.setString(5, phone);
            stmt.setString(6, vanNumber);
            stmt.executeUpdate();
            return getGeneratedId(stmt, "Driver", name);
        } catch (Exception e) {
            System.out.println("Failed to insert data: " + e.getMessage());
            return -1;
        }
    }

    public static void updateDriverField(int id, String fieldToUpdate, Object newValue) {
        // Check username uniqueness before updating
        if ("username".equals(fieldToUpdate) && isUserInDb(newValue.toString())) {
            System.out.println("New username already used");
            return;
        }

        String columnName = DRIVER_FIELD_MAP.get(fieldToUpdate);
        if (columnName == null) {
            System.out.println("Invalid field to update: " + fieldToUpdate);
            return;
        }

        String sql = "UPDATE Driver SET " + columnName + " = ? WHERE Driver_Id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, newValue.toString());
            stmt.setInt(2, id);
            stmt.executeUpdate();
            System.out.println(fieldToUpdate + " updated for Driver with ID: " + id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void deleteDriverData(int id) {
        String sql = "DELETE FROM Driver WHERE Driver_Id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
            System.out.println("Deleted Driver with ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to delete data: " + e.getMessage());
        }
    }

    public static Driver getDriverObject(int id) {
        String sql = "SELECT * FROM Driver WHERE Driver_Id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new Driver(
                            id,
                            rs.getString("Driver_Name"),
                            rs.getString("USERNAME"),
                            rs.getString("Driver_Password"),
                            rs.getString("Driver_Email"),
                            rs.getString("Driver_Phone"),
                            rs.getString("Driver_VanNo")
                    );
                }
            }
            System.out.println("No data found for ID: " + id);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return new Driver();
    }

    public static boolean isDriverInDb(String userName) {
        String sql = "SELECT 1 FROM Driver WHERE USERNAME = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
            System.out.println("No existing Driver with this Username");
            return false;
        } catch (Exception e) {
            System.out.println("Error checking Driver Username: " + e.getMessage());
            return false;
        }
    }

    public static Integer giveDriverGetId(String userName) {
        String sql = "SELECT Driver_Id FROM Driver WHERE USERNAME = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("Driver_Id");
                }
            }
            System.out.println("No Driver found with the given Username.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static String giveDriverGetPassword(String userName) {
        String sql = "SELECT Driver_Password FROM Driver WHERE USERNAME = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, userName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("Driver_Password");
                }
            }
            System.out.println("No Driver found with the given Username.");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
        return null;
    }

    public static int getDriverRowCount() {
        createDriverTable();
        String sql = "SELECT COUNT(*) AS row_count FROM Driver;";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("row_count");
            }
        } catch (Exception e) {
            System.out.println("Error retrieving row count: " + e.getMessage());
        }
        return -1;
    }

    // =========================================================================
    // Helper Methods
    // =========================================================================

    private static void executeUpdate(String sql, String tableName) {
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.executeUpdate();
            System.out.println("Table '" + tableName + "' reached");
        } catch (Exception e) {
            System.out.println("Failed to execute update: " + e.getMessage());
        }
    }

    private static int getGeneratedId(PreparedStatement stmt, String entityType, String name) throws Exception {
        try (ResultSet keys = stmt.getGeneratedKeys()) {
            if (keys.next()) {
                int id = keys.getInt(1);
                System.out.println("Inserted data of " + entityType + ": " + name + " with ID: " + id);
                return id;
            }
        }
        System.out.println("No ID obtained for the inserted data.");
        return -1;
    }
}
