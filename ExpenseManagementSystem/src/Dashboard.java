import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class Dashboard {
    private JFrame frame;
    private JPanel panel = new JPanel();

    public Dashboard(int id) {
        frame = new JFrame("Dashboard");
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        String name = null;
        try {
            Connection con = DatabaseConnection.getConnection();
            String selectSql = "select username from users where id=?";
            PreparedStatement pstmt = con.prepareStatement(selectSql);
            pstmt.setInt(1, id);
            ResultSet user = pstmt.executeQuery();
            if (user.next())
                name = user.getString("username");
        } catch (Exception exp) {
            DisplayMessage.errorMessage(frame, "Username not found", "Exception");
        }
        JLabel greeting = new JLabel("HI, " + name + ". Following is your total expense.");
        panel.add(greeting);
        greeting.setFont(new Font("Arial", Font.BOLD, 18));

        JButton addExpenseButton = new JButton("Add Expense");
        addExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                new AddExpense(id);
                frame.dispose();
            }
        });
        handleDashboard(id);
        panel.add(addExpenseButton);
        frame.add(panel);
    }

    private void handleDashboard(int id) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String getSql = "SELECT amount FROM expenses WHERE user_id=?";
            PreparedStatement pstmt = con.prepareStatement(getSql);
            pstmt.setInt(1, id);
            ResultSet getData = pstmt.executeQuery();

            double amount = 0;
            boolean hasData = false;

            while (getData.next()) {
                hasData = true;
                double value = getData.getDouble("amount");
                amount += value;
            }

            if (!hasData) {
                panel.add(new JLabel("You haven't made any Expenses Yet!"));
            } else {
                panel.add(new JLabel("Total Spent : Rs. " + amount));
                JButton showExpenseButton = new JButton("Show All Expenses");
                showExpenseButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new ShowExpense(id);
                        frame.dispose();
                    }
                });
                panel.add(showExpenseButton);
            }

        } catch (Exception e) {
            DisplayMessage.errorMessage(frame, e.getMessage(), "Exception Occured");
        }
    }
}
