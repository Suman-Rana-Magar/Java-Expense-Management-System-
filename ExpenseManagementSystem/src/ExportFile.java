import java.io.*;
import java.sql.*;
import javax.swing.*;

public class ExportFile {
    private ExportFile() {
    }

    public static void exportToCSV(JFrame frame, int userId, String userName) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from expenses where user_id=" + userId;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet expenses = stmt.executeQuery();

            FileWriter csvWriter = new FileWriter(userName.trim().replace(" ", "_") + "_expenses.csv");
            csvWriter.append("S.No.,Title,Amount,Category,Payment Mode,Date\n");
            int sno = 1;
            while (expenses.next()) {
                csvWriter.append(sno++ + ",");
                csvWriter.append(expenses.getString("title") + ",");
                csvWriter.append(expenses.getDouble("amount") + ",");
                csvWriter.append(expenses.getString("category") + ",");
                csvWriter.append(expenses.getString("payment_mode") + ",");
                csvWriter.append(expenses.getDate("expense_date").toString() + "\n");
            }
            csvWriter.flush();
            csvWriter.close();
            DisplayMessage.successMessage(frame, "File Exported Successfully !", "Export Success");
        } catch (Exception exp) {
            DisplayMessage.errorMessage(frame, exp.getMessage(), "Export error");
        }
    }

    public static void exportToText(JFrame frame, int userId, String userName) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from expenses where user_id=" + userId;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet expenses = stmt.executeQuery();

            FileWriter txtWriter = new FileWriter(userName.trim().replace(" ", "_") + "_expenses.txt");
            int sno = 1;
            while (expenses.next()) {
                txtWriter.write("Expense #" + sno++ + "\n");
                txtWriter.write("Title: " + expenses.getString("title") + "\n");
                txtWriter.write("Amount: " + expenses.getDouble("amount") + "\n");
                txtWriter.write("Category: " + expenses.getString("category") + "\n");
                txtWriter.write("Payment Mode: " + expenses.getString("payment_mode") + "\n");
                txtWriter.write("Date: " + expenses.getDate("expense_date").toString() + "\n");
            }
            txtWriter.flush();
            txtWriter.close();
            DisplayMessage.successMessage(frame, "File Exported Successfully !", "Export Success");
        } catch (Exception exp) {
            DisplayMessage.errorMessage(frame, exp.getMessage(), "Export error");
        }
    }
}
