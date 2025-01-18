import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String DATABASE_URL = "jdbc:sqlite:Database/Database.db";
    private static Connection connection;

    //*********************************************************************************************************************
//create new database or connect to existing one
    public static void connectToDatabase() {
        Connection Connect = null;
        try {
            Connect = DriverManager.getConnection(DATABASE_URL);
            System.out.println("Connected to database");

            // Enable foreign key support
            try (Statement st1 = Connect.createStatement()) {
                st1.execute("PRAGMA foreign_keys = ON;");
                System.out.println("Foreign key support enabled.");
            } catch (Exception e) {
                System.out.println("Failed to enable foreign key support: " + e.getMessage());
            }
        } catch (Exception e) {
            System.out.println("Error connecting to database: " + e.getMessage());
        }
        connection = Connect;
        // connection successful if connection static var != null
    }


    //*********************************************************************************************************************
//create Parent table in the database if not exist.
    private static void createParentTable() {
        if (connection == null) {
            System.out.println("Connection is null. Cannot create table.");
            return;
        }
        String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS Parent (
                        Parent_Id INTEGER PRIMARY KEY AUTOINCREMENT,
                        Parent_Name TEXT NOT NULL,
                        USERNAME TEXT NOT NULL,
                        Parent_Password TEXT NOT NULL,
                        Parent_Email TEXT NOT NULL,
                        Parent_Phone INTEGER NOT NULL,
                        Parent_NOS INTEGER NOT NULL
                        );
                """;

        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.executeUpdate();
            System.out.println("Table 'Parent' reached");
        } catch (Exception e) {
            System.out.println("Failed to execute update: " + e.getMessage());
        }
    }


    //*********************************************************************************************************************
//close connection with database
    public static void closeConnection() {
        try {
            if (!connection.isClosed()) {
                connection.close();
                System.out.println("Connection closed.");
            } else {
                System.out.println("Connection is already closed");
            }
        } catch (Exception e) {
            System.out.println("Failed to close connection: " + e.getMessage());
        } finally {
            connection = null; // Ensure the connection is set to null
        }
    }


    //*********************************************************************************************************************
//add Parent data to database
//parameters-(parent name,USERNAME,password,email,phone no,no of student)
//note-Username should have only one word
//id will be returned after data insertion,in any exception -1 will be returned.
    public static int insertParentData(String name, String username, String pwd, String email, int phone, int noOfStu) {
        createParentTable(); // Automatically create the table if it doesn't exist
        String insertSQL = "INSERT INTO Parent (Parent_Name, USERNAME, Parent_Password, Parent_Email, Parent_Phone, Parent_NOS) VALUES (?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, username);
            statement.setString(3, pwd);
            statement.setString(4, email);
            statement.setInt(5, phone);
            statement.setInt(6, noOfStu);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("Inserted data of Parent: " + name + " with ID: " + generatedId);
                    return generatedId; // Return the ID
                } else {
                    System.out.println("No ID obtained for the inserted data.");
                    return -1;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to insert data: " + e.getMessage());
            return -1;
        }
    }


    //*********************************************************************************************************************
//read Parent data in database
    public static void printParentTableData() {
        String selectSQL = "SELECT * FROM Parent;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Parent data in the database:");
            while (resultSet.next()) {
                int id = resultSet.getInt("Parent_Id");
                String name = resultSet.getString("Parent_Name");
                String username = resultSet.getString("USERNAME");
                String pwd = resultSet.getString("Parent_Password");
                String email = resultSet.getString("Parent_Email");
                int phnNum = resultSet.getInt("Parent_Phone");
                int parentNos = resultSet.getInt("Parent_NOS");
                System.out.printf("ID: %d, Name: %s, Username: %s, Password: %s, email: %s, Phone: %d,No of Students: %d%n", id, name, username, pwd, email, phnNum, parentNos);
            }
        } catch (Exception e) {
            System.out.println("Failed to read data: " + e.getMessage());
        }
    }

//*********************************************************************************************************************
//update data in parent table
//update single field. pass data in this order- (parent id,field that is needed to update(name or email etc),new updated value)
//field to update select from this,
//(name,username, pwd,email,phone,NOS) send in string format.
//warning - do not send any null value to parameter 3

    public static Boolean updateParentField(int id, String fieldToUpdate, Object newValue) {
        String updateSQL = "UPDATE Parent SET %s = ? WHERE Parent_Id = ?";
        String sql;

        switch (fieldToUpdate) {
            case "name":
                sql = String.format(updateSQL, "Parent_Name");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newValue.toString());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("Name updated" + " for Parent with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }
            case "username":
                if (isUserInDb(newValue.toString())) {
                    System.out.println("New username already used");
                    return false;
                } else {
                    System.out.println("New username available");
                    sql = String.format(updateSQL, "USERNAME");
                    try (PreparedStatement statement = connection.prepareStatement(sql)) {
                        statement.setString(1, newValue.toString());
                        statement.setInt(2, id);
                        statement.executeUpdate();
                        System.out.println("USERNAME updated" + " for Parent with ID: " + id);
                        return true;
                    } catch (Exception e) {
                        System.out.println("Error: " + e.getMessage());
                        return false;
                    }

                }
            case "pwd":
                sql = String.format(updateSQL, "Parent_Password");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newValue.toString());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("Password Updated" + " for Parent with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }
            case "email":
                sql = String.format(updateSQL, "Parent_Email");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newValue.toString());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("Email updated" + " for Parent with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }
            case "phone":
                sql = String.format(updateSQL, "Parent_Phone");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, (Integer) newValue);
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("Phone number updated" + " for Parent with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }
            case "NOS":
                sql = String.format(updateSQL, "Parent_NOS");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, (Integer) newValue);
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("No of students updated" + " for Parent with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }

            default:
                System.out.println("Invalid field to update: " + fieldToUpdate);
                return false;
        }
    }

    //*********************************************************************************************************************
//delete data in parent table
    public static void deleteParentData(int id) {
        String deleteSQL = "DELETE FROM Parent WHERE Parent_Id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Deleted Parent with ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to delete data: " + e.getMessage());
        }
    }

//*********************************************************************************************************************
//send data will be returned in an array of string
//string array size 6 order- Name,Username,Password,Email,Phone,no of stu

    public static String[] getParentData(int id) {
        String[] ParentData = {null, null, null, null, null, null};
        String selectSQL = "SELECT * FROM Parent WHERE Parent_Id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                ParentData[0] = resultSet.getString("Parent_Name");
                ParentData[1] = resultSet.getString("USERNAME");
                ParentData[2] = resultSet.getString("Parent_Password");
                ParentData[3] = resultSet.getString("Parent_Email");
                ParentData[4] = String.format("%d", resultSet.getInt("Parent_Phone"));
                ParentData[5] = String.format("%d", resultSet.getInt("Parent_NOS"));
                return ParentData;
            } else {
                System.out.println("No data found for ID: " + id);
                return ParentData;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return ParentData;
        }
    }

    //*********************************************************************************************************************
// Check if a specific (username) exists in the database
// // If it exists, it returns true, else false.
    public static boolean isUserInDb(String userName) {
        String selectSQL = "SELECT USERNAME FROM Parent WHERE USERNAME = ?;";

        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, userName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return true;
            } else {
                System.out.println("No existing User in this UserName");
                return false;
            }
        } catch (Exception e) {
            System.out.println("Error checking User Name: " + e.getMessage());
            return false;
        }
    }


//*********************************************************************************************************************
//give username get password, of none get null

    public static String giveUserGetPassword(String userName) {
        String selectSQL = "SELECT * FROM Parent WHERE USERNAME= ?;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, userName);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getString("Parent_Password");
            } else {
                return null;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return null;
        }
    }

//*********************************************************************************************************************
//give username get id, exception  get 0

    public static int giveUserGetId(String userName) {
        String selectSQL = "SELECT * FROM Parent WHERE USERNAME= ?;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, userName);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("Parent_Id");
            } else {
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

//*********************************************************************************************************************
//give phoneNo get id, exception  get 0

    public static int givePhoneGetId(int phone) {
        String selectSQL = "SELECT * FROM Parent WHERE Parent_Phone= ?;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setInt(1, phone);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("Parent_Id");
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }
//*********************************************************************************************************************
//give email get id, exception  get 0

    public static int giveEmailGetId(String email) {
        String selectSQL = "SELECT * FROM Parent WHERE Parent_Email= ?;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getInt("Parent_Id");
            } else {
                return 0;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            return 0;
        }
    }

    //*********************************************************************************************************************
//Student related methods
//*********************************************************************************************************************
//create Student table in the database if not exist.
    private static void createStuTable() {
        if (connection == null) {
            System.out.println("Connection is null. Cannot create table.");
            return;
        }
        String createTableSQL = """
                    CREATE TABLE IF NOT EXISTS Student (
                        Student_Id INTEGER PRIMARY KEY AUTOINCREMENT,
                        Student_Name TEXT NOT NULL,
                        Student_Age INTEGER NOT NULL,
                        Student_Address TEXT NOT NULL,
                        Student_School TEXT NOT NULL,
                        Student_Teacher_Number INTEGER NOT NULL,
                        Student_Status TEXT NOT NULL,
                        Parent_Id INTEGER NOT NULL,
                        FOREIGN KEY (Parent_Id) REFERENCES Parent (Parent_Id) ON DELETE CASCADE ON UPDATE CASCADE
                        );
                """;

        try (PreparedStatement statement = connection.prepareStatement(createTableSQL)) {
            statement.executeUpdate();
            System.out.println("Table 'Student' reached.");
        } catch (Exception e) {
            System.out.println("Failed to execute update: " + e.getMessage());
        }
    }

    //*********************************************************************************************************************
//add Student data to database
//parameters-(stu name,age,address,school,Teacher num,studentStatus,parent ID)
//id will be returned after data insertion,in any exception -1 will be returned.
    public static int insertStuData(String name, int age,String address,String school,int teacherNum,String stuStatus,int parentId) {
        createStuTable(); // Automatically create the table if it doesn't exist
        String insertSQL = "INSERT INTO Student (Student_Name, Student_Age, Student_Address, Student_School, Student_Teacher_Number, Student_Status, Parent_Id) VALUES (?, ?, ?, ?, ?, ?, ?);";
        try (PreparedStatement statement = connection.prepareStatement(insertSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setInt(2, age);
            statement.setString(3, address);
            statement.setString(4, school);
            statement.setInt(5, teacherNum);
            statement.setString(6, stuStatus);
            statement.setInt(7, parentId);
            statement.executeUpdate();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    System.out.println("Inserted data of Student: " + name + " with ID: " + generatedId);
                    return generatedId; // Return the ID
                } else {
                    System.out.println("No ID obtained for the inserted data.");
                    return -1;
                }
            }
        } catch (Exception e) {
            System.out.println("Failed to insert data: " + e.getMessage());
            return -1;
        }
    }

    //*********************************************************************************************************************
//read Stu data in database
    public static void printStuTableData() {
        String selectSQL = "SELECT * FROM Student;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL);
             ResultSet resultSet = statement.executeQuery()) {

            System.out.println("Student data in the database:");
            while (resultSet.next()) {
                int id=resultSet.getInt("Student_Id");
                String name =resultSet.getString("Student_Name");
                int age =resultSet.getInt("Student_Age");
                String address=resultSet.getString("Student_Address");
                String school=resultSet.getString("Student_School");
                int teacherNum=resultSet.getInt("Student_Teacher_Number");
                String stuStatus=resultSet.getString("Student_Status");
                int parentId=resultSet.getInt("Parent_Id");

                System.out.printf("ID: %d, Name: %s, Age: %s, Address: %s, School: %s, Teacher Number: %d,Student Status: %s,Parent Id: %d%n", id, name, age, address, school, teacherNum, stuStatus, parentId);
            }
        } catch (Exception e) {
            System.out.println("Failed to read data: " + e.getMessage());
        }
    }

    //*********************************************************************************************************************
//update data in Student table
//update single field. pass data in this order- (stu id,field that is needed to update(name or email etc),new updated value)
//field to update select from this,
//(name,age,address,school,teacherNum,stuStatus,parentId) send in string format.
//warning - do not send any null value to parameter 3
//will return true of success, else return false -error
    public static Boolean updateStuField(int id, String fieldToUpdate, Object newValue) {
        String updateSQL = "UPDATE Student SET %s = ? WHERE Student_Id = ?";
        String sql;

        switch (fieldToUpdate) {
            case "name":
                sql = String.format(updateSQL, "Student_Name");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newValue.toString());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("Name updated" + " for Student with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }
            case "age":
                sql = String.format(updateSQL, "Student_Age");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newValue.toString());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("Age updated" + " for Student with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }

            case "address":
                sql = String.format(updateSQL, "Student_Address");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newValue.toString());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("Address updated" + " for Student with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }
            case "school":
                sql = String.format(updateSQL, "Student_School");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newValue.toString());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("School updated" + " for Student with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }
            case "teacherNum":
                sql = String.format(updateSQL, "Student_Teacher_Number");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, (Integer) newValue);
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("Teacher number updated" + " for Student with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }
            case "stuStatus":
                sql = String.format(updateSQL, "Student_Status");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setString(1, newValue.toString());
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("Student Status updated" + " for Student with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }
            case "parentId":
                sql = String.format(updateSQL, "Parent_Id");
                try (PreparedStatement statement = connection.prepareStatement(sql)) {
                    statement.setInt(1, (Integer) newValue);
                    statement.setInt(2, id);
                    statement.executeUpdate();
                    System.out.println("Parent Id " + " for Student with ID: " + id);
                    return true;
                } catch (Exception e) {
                    System.out.println("Error: " + e.getMessage());
                    return false;
                }

            default:
                System.out.println("Invalid field to update: " + fieldToUpdate);
                return false;
        }
    }

    //*********************************************************************************************************************
//delete data in parent table
    public static void deleteStuData(int id) {
        String deleteSQL = "DELETE FROM Student WHERE Student_Id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(deleteSQL)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Deleted Student with ID: " + id);
        } catch (Exception e) {
            System.out.println("Failed to delete data: " + e.getMessage());
        }
    }

    //*********************************************************************************************************************
//send data will be returned in an array of string
//string array size 7 order -stu name,age,address,school,Teacher num,studentStatus,parent ID

    public static String[] getStuData(int id) {
        String[] StuData = {null, null, null, null, null, null, null};
        String selectSQL = "SELECT * FROM Student WHERE Student_Id = ?;";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setInt(1, id);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {

                StuData[0] = resultSet.getString("Student_Name");
                StuData[1] = resultSet.getString("Student_Age");
                StuData[2] = resultSet.getString("Student_Address");
                StuData[3] = resultSet.getString("Student_School");
                StuData[4] = String.format("%d", resultSet.getInt("Student_Teacher_Number"));
                StuData[5] = resultSet.getString("Student_Status");
                StuData[6] = String.format("%d", resultSet.getInt("Parent_Id"));
            } else {
                System.out.println("No data found for ID: " + id);

            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());

        }
        return StuData;
    }
    //*********************************************************************************************************************
//parameters receive name of the school, and get all ids of students in the sent school name.
//string array size dynamic (list of ids.) array is in int form.

    public static List<Integer> giveSchoolGetStuList(String school) {
        List<Integer> stuInSchool = new ArrayList<>();
        String selectSQL = "SELECT Student_Id FROM Student WHERE Student_School = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setString(1, school); // Set the school name in the query
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                stuInSchool.add(resultSet.getInt("Student_Id"));
            }
        }
        catch (Exception e) {
            System.out.println("Failed to retrieve student IDs: " + e.getMessage());
        }
        return stuInSchool;
    }
    //*********************************************************************************************************************
//parameters receive id of the parent, and get id's of Student's related to parent Id.
//string array size dynamic (list of ids.) array is in int form.
    public static List<Integer> giveParentIdGetStuId(int parentId) {
        List<Integer> stuOfParent= new ArrayList<>();
        String selectSQL = "SELECT Student_Id FROM Student WHERE Parent_Id = ?";
        try (PreparedStatement statement = connection.prepareStatement(selectSQL)) {
            statement.setInt(1, parentId); // Set the school name in the query
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                stuOfParent.add(resultSet.getInt("Student_Id"));
            }
        }
        catch (Exception e) {
            System.out.println("Failed to retrieve student IDs: " + e.getMessage());
        }
        return stuOfParent;
    }

}