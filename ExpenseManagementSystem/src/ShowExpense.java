import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ShowExpense {
    JFrame frame = new JFrame("Show Expenses");
    JPanel panel = new JPanel();

    public ShowExpense(int userId) {
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from expenses where user_id=" + userId;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet expenses = stmt.executeQuery();
            String[] columnName = { "S.No.", "Title", "Amount", "Category", "Payment Mode", "Date" };
            DefaultTableModel model = new DefaultTableModel(columnName, 0);
            int sNo = 0;
            while (expenses.next()) {
                sNo++;
                String title = expenses.getString("title");
                double amount = expenses.getDouble("amount");
                String category = expenses.getString("category");
                String paymentMode = expenses.getString("payment_mode");
                Date expDate = expenses.getDate("expense_date");
                Object[] row = { sNo, title, amount, category, paymentMode, expDate };
                model.addRow(row);
            }

            JTable table = new JTable(model);
            JScrollPane scrollPane = new JScrollPane(table);

            JButton dashboardButton = new JButton("Return to Dashboard");
            dashboardButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    new Dashboard(userId);
                    frame.dispose();
                }
            });
            panel.add(scrollPane);
            panel.add(dashboardButton);
            frame.add(panel);
        } catch (Exception exp) {
            DisplayMessage.errorMessage(frame, exp.getMessage(), "Exception");
        }
    }
}
