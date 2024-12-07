import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.ResultSet;

public class TestOracleJDBC {


    public static void main(String[] args) {
        String jdbcUrl = "jdbc:oracle:thin:@myoracle12c.senecacollege.ca:1521/oracle12c";
        String username = "dbs211_242nee13";
        String password = "32041417";

        try {
            // Load Oracle JDBC Driver
            Class.forName("oracle.jdbc.driver.OracleDriver");
            System.out.println("Oracle JDBC Driver Registered!");

            // Establish the connection
            Connection connection = DriverManager.getConnection(jdbcUrl, username, password);
            System.out.println("Connection established successfully!");
            // Create a Statement object
            Statement statement = connection.createStatement();

            String query1 = "SELECT employeenumber, firstname, lastname, phone, extension " +
                    "FROM dbs211_employees, dbs211_offices " +
                    "WHERE city = 'San Francisco' AND dbs211_employees.officecode = dbs211_offices.officecode " +
                    "ORDER BY employeenumber";
            ResultSet rs1 = statement.executeQuery(query1);
            System.out.println("\nReport 1 (Employee Report)");
            System.out.printf("%-15s %-15s %-15s %-20s %-15s\n", "EmployeeNumber", "FirstName", "LastName", "Phone", "Extension");
            while (rs1.next()) {
                int employeeNumber = rs1.getInt("employeenumber");
                String firstName = rs1.getString("firstname");
                String lastName = rs1.getString("lastname");
                String phone = rs1.getString("phone");
                String extension = rs1.getString("extension");
                System.out.printf("%-15d %-15s %-15s %-20s %-15s\n", employeeNumber, firstName, lastName, phone, extension);
            }
            rs1.close();

            String query2 = "SELECT DISTINCT dbs211_employees.employeenumber, dbs211_employees.lastname, dbs211_offices.phone, dbs211_employees.extension " +
                    "FROM dbs211_employees, dbs211_offices " +
                    "WHERE dbs211_employees.officecode = dbs211_offices.officecode " +
                    "AND dbs211_employees.employeenumber IN (SELECT DISTINCT reportsto FROM dbs211_employees)";
            ResultSet rs2 = statement.executeQuery(query2);
            System.out.println("\nReport 2 (Managers Report)");
            System.out.printf("%-15s %-15s %-20s %-15s\n", "EmployeeNumber", "LastName", "Phone", "Extension");
            while (rs2.next()) {
                int employeeNumber = rs2.getInt("employeenumber");
                String lastName = rs2.getString("lastname");
                String phone = rs2.getString("phone");
                String extension = rs2.getString("extension");
                System.out.printf("%-15d %-15s %-20s %-15s\n", employeeNumber, lastName, phone, extension);
            }
            rs2.close();

            // Close the connection
            connection.close();
            System.out.println("Connection closed!");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Connection failed!");
            e.printStackTrace();
        }
    }
}
