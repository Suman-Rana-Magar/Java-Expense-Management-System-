import java.sql.*;

public class DatabaseConnection {
    // Declare a static Connection object
    private static Connection connection;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
    }

    // Static method to get the single instance of the database connection
    public static Connection getConnection() {
        if (connection == null) {
            try {
                // Load database driver (if needed, for example for MySQL)
                Class.forName("com.mysql.cj.jdbc.Driver");

                // Establish a new connection if it doesn't already exist
                connection = DriverManager.getConnection("jdbc:mysql://localhost:33061/expense_mgnt", "root", "root");
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return connection; // Return the existing connection
    }

    // Optional: method to close the connection (you can call this when shutting down the app)
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
