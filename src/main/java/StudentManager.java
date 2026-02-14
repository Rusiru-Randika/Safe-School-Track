import java.util.List;
import java.util.Scanner;

class StudentManager {

    public static void sortFromSchool(Scanner scn) {
        List<String> schoolList = DatabaseManager.getDistinctSchoolTypes();
        int schoolCount = schoolList.size();

        for (int a = 0; a < schoolCount; a++) {
            System.out.println("Select school: ");
            for (int i = 0; i < schoolCount; i++) {
                System.out.println((i + 1) + ") " + schoolList.get(i));
            }
            System.out.println("Press 0 to exit");
            int choose = scn.nextInt();

            if (choose == 0) {
                return;
            }

            outerLoop:
            while (true) {
                if (choose <= 0 || choose > schoolCount) {
                    System.out.println("Please choose from the valid range (1 to " + schoolCount + ").");
                    System.out.print("Enter your choice: ");
                    choose = scn.nextInt();
                    scn.nextLine();
                    continue;
                }

                System.out.println(schoolList.get(choose - 1) + " school selected.");
                List<Integer> studentIds = DatabaseManager.giveSchoolGetStuList(schoolList.get(choose - 1));

                for (Integer studentId : studentIds) {
                    changeStudentStatus(studentId, scn);
                }

                System.out.println("Are all students' statuses updated?");
                System.out.println("1) Yes\n2) No");
                int confirm = 0;

                while (confirm != 1 && confirm != 2) {
                    System.out.print("Enter your choice (1 or 2): ");
                    confirm = scn.nextInt();
                    if (confirm != 1 && confirm != 2) {
                        System.out.println("Please choose correctly (1 or 2).");
                    }
                }

                if (confirm == 1) {
                    break outerLoop;
                }
            }
        }

        displayStudentTable();
    }

    public static void changeStudentStatus(int id, Scanner scn) {
        String[] studentData = DatabaseManager.getStuData(id);
        printStudentRow(studentData);

        int option = 0;
        while (option < 1 || option > 7) {
            System.out.println("Select new status:");
            System.out.println("1. Dropped at home");
            System.out.println("2. Dropped at school");
            System.out.println("3. Picked at home");
            System.out.println("4. Picked at school");
            System.out.println("5. There is an issue - Contact Teacher");
            System.out.println("6. Ignore/Pass");
            System.out.println("7. Type special message");
            System.out.print("Enter option: ");

            if (scn.hasNextInt()) {
                option = scn.nextInt();
                scn.nextLine();
                if (option < 1 || option > 7) {
                    System.out.println("Invalid option. Please enter a number between 1 and 7.");
                }
            } else {
                System.out.println("Invalid input. Please enter a number between 1 and 7.");
                scn.next();
            }
        }

        if (option == 6) {
            System.out.println("No changes made to the student status.");
            return;
        }

        String newStatus = switch (option) {
            case 1 -> "Driver Message - Dropped at home";
            case 2 -> "Driver Message - Dropped at school";
            case 3 -> "Driver Message - Picked at home";
            case 4 -> "Driver Message - Picked at school";
            case 5 -> "Driver Message - There is an issue, Contact Teacher";
            case 7 -> {
                System.out.print("Type special message: ");
                yield "Driver Message - " + scn.nextLine();
            }
            default -> null;
        };

        if (newStatus != null) {
            boolean result = DatabaseManager.updateStuField(id, "Student_Status", newStatus);
            System.out.println(result ? "Student status updated successfully." : "Failed to update student status.");
        }
    }

    public static void displayStudentTable() {
        System.out.println("Student Data from Database:");
        DatabaseManager.printStuTableData();
    }

    private static void printStudentRow(String[] data) {
        System.out.println("+------------------+------------------+------------------+------------------+------------------+-------------------------------------------------------+------------------+");
        System.out.println("| Student Name     | Age              | Address          | School           | Teacher Number   | Status                                                | Parent ID        |");
        System.out.println("+------------------+------------------+------------------+------------------+------------------+-------------------------------------------------------+------------------+");
        System.out.printf("| %-16s | %-16s | %-16s | %-16s | %-16s | %-55s| %-16s |%n",
                data[0], data[1], data[2], data[3], data[4], data[5], data[6]);
        System.out.println("+------------------+------------------+------------------+------------------+------------------+-------------------------------------------------------+------------------+");
    }
}
