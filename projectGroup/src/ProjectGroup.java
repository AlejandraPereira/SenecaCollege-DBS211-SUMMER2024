import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;
import java.util.Scanner;
import java.sql.PreparedStatement;

public class ProjectGroup {

    public static void main(String[] args) {

        String jdbcUrl = "jdbc:oracle:thin:@myoracle12c.senecacollege.ca:1521:oracle12c";
        String username = "dbs211_242nee13";
        String password = "32041417";

        Connection conn = null;

        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Oracle JDBC Driver Registered!");
            // Establish the connection
            conn = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connection established successfully!");

            // Perform a query
            int choice;
            do {
                choice = menu();
                switch (choice) {
                    case 1:
                        findEmployee(conn);
                        break;
                    case 2:
                        displayAllEmployees(conn);
                        break;
                    case 3:
                        insertEmployee(conn);
                        break;
                    case 4:
                        updateEmployee(conn);
                        break;
                    case 5:
                        deleteEmployee(conn);
                        break;
                    case 0:
                        System.out.println("Exiting...");
                        break;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } while (choice != 0);


        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Error connecting to the database.");
            System.out.println("Error code: " + e.getErrorCode());
            System.out.println("Error message: " + e.getMessage());
        } finally {
            // Close the connection
            if (conn != null) {
                try {
                    conn.close();
                    System.out.println("Connection closed successfully.");
                } catch (SQLException e) {
                    System.out.println("Error closing the connection.");
                    System.out.println("Error code: " + e.getErrorCode());
                    System.out.println("Error message: " + e.getMessage());
                }
            }
        }
    }

    //MILESTONE 1
    public static int menu() {
        Scanner input = new Scanner(System.in);
        int choice;
        do {
            System.out.println("****** HR Menu ******");
            System.out.println("1: Find Employee");
            System.out.println("2: Employees Report");
            System.out.println("3: Add Employee");
            System.out.println("4: Update Employee");
            System.out.println("5: Remove Employee");
            System.out.println("0: Exit");
            System.out.println("Select an option between 0 and 5");
            choice = input.nextInt();
        } while (choice < 0 || choice > 5);
        return choice;
    }

    public static class Employee {
        int employeeNumber;
        String lastName;
        String firstName;
        String extension;
        String email;
        String officeCode;
        String jobTitle;
        int reportsTo;

        public Employee() {
        }

        public Employee(int employeeNumber, String lastName, String firstName, String extension, String email, String officeCode, String jobTitle, int reportsTo) {
            this.employeeNumber = employeeNumber;
            this.lastName = lastName;
            this.firstName = firstName;
            this.extension = extension;
            this.email = email;
            this.officeCode = officeCode;
            this.jobTitle = jobTitle;
            this.reportsTo = reportsTo;
        }
        
    }

    public static void findEmployee(Connection conn) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter employee number: ");
        int employeeNumber = input.nextInt();
        String query = "SELECT * FROM dbs211_employees WHERE employeeNumber = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeNumber);
            ResultSet resultSet = pstmt.executeQuery();
            if (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("employeeNumber"),
                        resultSet.getString("lastName"),
                        resultSet.getString("firstName"),
                        resultSet.getString("extension"),
                        resultSet.getString("email"),
                        resultSet.getString("officeCode"),
                        resultSet.getString("jobTitle"),
                        resultSet.getInt("reportsTo")
                );
                displayEmployee(employee);
            } else {
                System.out.println("Employee not found.");
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println("Error executing query.");
            System.out.println("Error code: " + e.getErrorCode());
            System.out.println("Error message: " + e.getMessage());
        }
    }

    public static void displayEmployee(Employee employee) {
        System.out.println();
        System.out.println("----- Employee Information -----");
        System.out.println("Employee Number: " + employee.employeeNumber);
        System.out.println("Last Name: " + employee.lastName);
        System.out.println("First Name: " + employee.firstName);
        System.out.println("Extension: " + employee.extension);
        System.out.println("Email: " + employee.email);
        System.out.println("Office Code: " + employee.officeCode);
        System.out.println("Manager ID: " + employee.reportsTo);
        System.out.println("Job Title: " + employee.jobTitle);
        System.out.println();
    }

    public static void displayAllEmployees(Connection conn) {
        String query = "SELECT * FROM dbs211_employees";
        try (Statement stmt = conn.createStatement()) {
            ResultSet resultSet = stmt.executeQuery(query);

            // Print table header
            System.out.printf("%-15s %-15s %-15s %-10s %-30s %-13s %-12s %-10s\n",
                    "EmployeeNumber", "Last Name", "First Name", "Extension", "Email", "OfficeCode", "ReportsTo", "JobTitle");
            System.out.println("---------------------------------------------------------------------------------------------------------------------------------");

            boolean hasResults = false;
            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("employeeNumber"),
                        resultSet.getString("lastName"),
                        resultSet.getString("firstName"),
                        resultSet.getString("extension"),
                        resultSet.getString("email"),
                        resultSet.getString("officeCode"),
                        resultSet.getString("jobTitle"),
                        resultSet.getInt("reportsTo")
                );

                // Print each row of the table
                System.out.printf("%-15d %-15s %-15s %-10s %-35s %-10s %-10d %-10s\n",
                        employee.employeeNumber,
                        employee.lastName,
                        employee.firstName,
                        employee.extension,
                        employee.email,
                        employee.officeCode,
                        employee.reportsTo,
                        employee.jobTitle);

                hasResults = true;
            }

            if (!hasResults) {
                System.out.println("No employees found.");
            }
            resultSet.close();
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + ": " + e.getMessage());
        }
        System.out.println();
    }

    //MILESTONE 2

    public static void getEmployeeDetails(Employee employee) {
        Scanner input = new Scanner(System.in);
        System.out.println("-------- New Employee Information --------");
        System.out.print("Employee Number: ");
        employee.employeeNumber = input.nextInt();
        input.nextLine(); // Consume the newline

        System.out.print("Last Name: ");
        employee.lastName = input.nextLine();

        System.out.print("First Name: ");
        employee.firstName = input.nextLine();

        System.out.print("Extension: ");
        employee.extension = input.nextLine();

        System.out.print("Email: ");
        employee.email = input.nextLine();

        System.out.print("Office Code: ");
        employee.officeCode = input.nextLine();

        System.out.print("Job Title: ");
        employee.jobTitle = input.nextLine();

        System.out.print("Reports To: ");
        employee.reportsTo =  input.nextInt();
    }

    public static void insertEmployee(Connection conn) {
        Employee employee = new Employee();
        getEmployeeDetails(employee);
        if (findEmployeeByNumber(conn, employee.employeeNumber)) {
            System.out.println("An employee with the same employee number exists.");
            return;
        }
        String query = "INSERT INTO dbs211_employees (employeeNumber, lastName, firstName, extension, email, officeCode, reportsTo, jobTitle) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employee.employeeNumber);
            pstmt.setString(2, employee.lastName);
            pstmt.setString(3, employee.firstName);
            pstmt.setString(4, employee.extension);
            pstmt.setString(5, employee.email);
            pstmt.setString(6, employee.officeCode);
            pstmt.setInt(7, employee.reportsTo);
            pstmt.setString(8, employee.jobTitle);
            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println();
                System.out.println("A new employee has been inserted successfully.");
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + ": " + e.getMessage());
        }
    }

    public static boolean findEmployeeByNumber(Connection conn, int employeeNumber) {
        String query = "SELECT COUNT(*) FROM dbs211_employees WHERE employeeNumber = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeNumber);
            ResultSet resultSet = pstmt.executeQuery();
            resultSet.next();
            int count = resultSet.getInt(1);
            resultSet.close();
            return count > 0;
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + ": " + e.getMessage());
            return false;
        }
    }

    public static void updateEmployee(Connection conn) {
        Scanner input = new Scanner(System.in);
        System.out.print("Enter employee number to update: ");
        int employeeNumber = input.nextInt();
        input.nextLine(); // Consume the newline

        if (!findEmployeeByNumber(conn, employeeNumber)) {
            System.out.println();
            System.out.println("Employee not found.");
            return;
        }
        System.out.println();
        System.out.println("-------- Update Employee Information --------");

        System.out.print("Last Name: ");
        String lastName = input.nextLine();

        System.out.print("First Name: ");
        String firstName = input.nextLine();

        System.out.print("Extension: ");
        String extension = input.nextLine();

        System.out.print("Email: ");
        String email = input.nextLine();

        System.out.print("Office Code: ");
        String officeCode = input.nextLine();

        System.out.print("Job Title: ");
        String jobTitle = input.nextLine();

        System.out.print("Manager ID: ");
        int reportsTo = input.nextInt();

        String query = "UPDATE dbs211_employees SET lastName = ?, firstName = ?, extension = ?, email = ?, officeCode = ?, reportsTo = ?, jobTitle = ? WHERE employeeNumber = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, lastName);
            pstmt.setString(2, firstName);
            pstmt.setString(3, extension);
            pstmt.setString(4, email);
            pstmt.setString(5, officeCode);
            pstmt.setInt(6, reportsTo);
            pstmt.setString(7, jobTitle);
            pstmt.setInt(8, employeeNumber);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee information updated successfully.");
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + ": " + e.getMessage());
        }
    }

    public static void deleteEmployee(Connection conn) {
        Scanner input = new Scanner(System.in);
        System.out.println();
        System.out.print("Enter employee number to delete: ");
        int employeeNumber = input.nextInt();

        if (!findEmployeeByNumber(conn, employeeNumber)) {
            System.out.println("Employee not found.");
            return;
        }

        String query = "DELETE FROM dbs211_employees WHERE employeeNumber = ?";

        try (PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setInt(1, employeeNumber);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println();
                System.out.println("Employee deleted successfully.");
            }
        } catch (SQLException e) {
            System.out.println(e.getErrorCode() + ": " + e.getMessage());
        }
    }
}
