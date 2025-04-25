import java.sql.*;

class Registration {

}

public class ExpenseMgntSystem {
    public static void main(String args[]) {
        try {
            Connection con = DatabaseConnection.getConnection();
            Statement stmt = con.createStatement();
            stmt.executeUpdate(
                    "insert into users values (1, 'john_doe', 'mypassword123', '2025-04-24 10:00:00', '2025-04-24 10:00:00')");
            DatabaseConnection.closeConnection();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
