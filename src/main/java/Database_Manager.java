import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class Database_Manager {
    private static final String DATABASE_URL = "jdbc:sqlite:C:/Users/Rusiru/Desktop/Project/Project2/src/Database/Database.db";
    private static Connection connection;

    public static void connectToDatabase() {
        try {
            Class.forName("org.sqlite.JDBC"); // Ensure the SQLite JDBC driver is loaded
            connection = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connected to database");
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
    }

    private static void createParentTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS Parent (
                "Parent_Id" INTEGER NOT NULL UNIQUE,
                "Parent_Name" TEXT NOT NULL,
                "Parent_Password" TEXT NOT NULL,
                "Parent_Email" TEXT NOT NULL,
                "Parent_Phone" INTEGER NOT NULL,
                CONSTRAINT "Parent_Id" PRIMARY KEY("Parent_Id" AUTOINCREMENT) ON CONFLICT REPLACE
            );
        """;

        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.executeUpdate();
            System.out.println("Table 'Parent' created or already exists.");
        } catch (Exception e) {
            System.out.println("Failed to execute update: " + e.getMessage());
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            } else {
                System.out.println("Connection is already closed");
            }
        } catch (Exception e) {
            System.out.println("Failed to close connection: " + e.getMessage());
        } finally {
            connection = null;
        }
    }

    public static void insertParentData(String name, String pwd, int phnNum, int stuId, String stuName) {
        createParentTable();
        String insertSQL = "INSERT INTO Parent (name, password, phone_number, studentId, studentName) VALUES (?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, name);
            statement.setString(2, pwd);
            statement.setInt(3, phnNum);
            statement.setInt(4, stuId);
            statement.setString(5, stuName);
            statement.executeUpdate();
            System.out.println("Inserted data of Parent: " + name);
        } catch (Exception e) {
            System.out.println("Failed to insert data: " + e.getMessage());
        }
    }

    public static void PrintParentTableData() {
        String selectSQL = "SELECT * FROM Parent;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Parent data in the database:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String pwd = resultSet.getString("password");
                int phnNum = resultSet.getInt("phone_number");
                int stuId = resultSet.getInt("studentId");
                String stuName = resultSet.getString("studentName");
                System.out.printf("ID: %d, Name: %s, Password: %s, Phone Number: %d, Student ID: %d, Student Name: %s%n", id, name, pwd, phnNum, stuId, stuName);
            }
        } catch (Exception e) {
            System.out.println("Failed to read data: " + e.getMessage());
        }
    }

    public static void updateParent(int id, String name, String pwd, int phnNum, int stuId, String stuName) {
        String updateSQL = "UPDATE Parent SET name = ?, password = ?, phone_number = ?, studentId = ?, studentName = ? WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, name);
            statement.setString(2, pwd);
            statement.setInt(3, phnNum);
            statement.setInt(4, stuId);
            statement.setString(5, stuName);
            statement.setInt(6, id);
            statement.executeUpdate();
            System.out.println("Updated Parent with ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to update data: " + e.getMessage());
        }
    }

    public static void deleteParentData(int id) {
        String deleteSQL = "DELETE FROM Parent WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Deleted Parent with ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to delete data: " + e.getMessage());
        }
    }

    public static String getParentData(int id, String type) {
        String selectSQL = "SELECT * FROM Parent WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                switch (type) {
                    case "name":
                        return resultSet.getString("name");
                    case "password":
                        return resultSet.getString("password");
                    case "phoneNo":
                        return String.format("%d", resultSet.getInt("phone_number"));
                    case "studentId":
                        return String.format("%d", resultSet.getInt("studentId"));
                    case "studentName":
                        return resultSet.getString("studentName");
                    case "all":
                        return resultSet.getString("name") + " " + resultSet.getString("password") + " " +
                                String.format("%d", resultSet.getInt("phone_number")) + " " +
                                String.format("%d", resultSet.getInt("studentId")) + " " +
                                resultSet.getString("studentName");
                    default:
                        return null;
                }
            } else {
                System.out.println("No data found for ID: " + id);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

    public static void createStudentTable() {
        String createTableSQL = """
            CREATE TABLE IF NOT EXISTS Student (
               id INTEGER PRIMARY KEY AUTOINCREMENT,
               name TEXT NOT NULL,
               school TEXT NOT NULL,
               grade TEXT NOT NULL,
               teacher_number INTEGER NOT NULL,
               address TEXT NOT NULL,
               email TEXT NOT NULL
            );
        """;

        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.executeUpdate();
            System.out.println("Table 'Student' created or already exists.");
        } catch (Exception e) {
            System.out.println("Failed to execute update: " + e.getMessage());
        }
    }

    public static void insertStudentData(String name, String school, String grade, int teacherNum, String address, String email) {
        createStudentTable();
        String insertSQL = "INSERT INTO Student (name, school, grade, teacher_number, address, email) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL)) {
            statement.setString(1, name);
            statement.setString(2, school);
            statement.setString(3, grade);
            statement.setInt(4, teacherNum);
            statement.setString(5, address);
            statement.setString(6, email);
            statement.executeUpdate();
            System.out.println("Inserted data of Student: " + name);
        } catch (Exception e) {
            System.out.println("Failed to insert data: " + e.getMessage());
        }
    }

    public static void PrintStudentTableData() {
        String selectSQL = "SELECT * FROM Student;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Student data in the database:");
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String name = resultSet.getString("name");
                String school = resultSet.getString("school");
                String grade = resultSet.getString("grade");
                int teacherNum = resultSet.getInt("teacher_number");
                String address = resultSet.getString("address");
                String email = resultSet.getString("email");
                System.out.printf("ID: %d, Name: %s, School: %s, Grade: %s, Teacher phone No: %d, address: %s, email: %s%n", id, name, school, grade, teacherNum, address, email);
            }
        } catch (Exception e) {
            System.out.println("Failed to read data: " + e.getMessage());
        }
    }

    public static void updateStudent(int id, String name, String school, String grade, int teacherNum, String address, String email) {
        String updateSQL = "UPDATE Student SET name = ?, school = ?, grade = ?, teacher_number = ?, address = ?, email = ? WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(updateSQL)) {
            statement.setString(1, name);
            statement.setString(2, school);
            statement.setString(3, grade);
            statement.setInt(4, teacherNum);
            statement.setString(5, address);
            statement.setString(6, email);
            statement.setInt(7, id);
            statement.executeUpdate();
            System.out.println("Updated Student with ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to update data: " + e.getMessage());
        }
    }

    public static void deleteStudentData(int id) {
        String deleteSQL = "DELETE FROM Student WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Deleted Student with ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to delete data: " + e.getMessage());
        }
    }

    public static String getStudentData(int id, String type) {
        String selectSQL = "SELECT * FROM Student WHERE id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                switch (type) {
                    case "name":
                        return resultSet.getString("name");
                    case "school":
                        return resultSet.getString("school");
                    case "grade":
                        return resultSet.getString("grade");
                    case "teacherNum":
                        return String.format("%d", resultSet.getInt("teacher_number"));
                    case "address":
                        return resultSet.getString("address");
                    case "email":
                        return resultSet.getString("email");
                    case "all":
                        return resultSet.getString("name") + " " + resultSet.getString("school") + " " +
                                resultSet.getString("grade") + " " +
                                String.format("%d", resultSet.getInt("teacher_number")) + " " +
                                resultSet.getString("address") + " " + resultSet.getString("email");
                    default:
                        return null;
                }
            } else {
                System.out.println("No data found for ID: " + id);
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }
}

