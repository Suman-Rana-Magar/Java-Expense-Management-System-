import javax.swing.*;

import java.awt.*;
import java.sql.*;
import java.awt.event.*;

public class Dashboard {
    private JFrame frame;
    private JPanel panel = new JPanel();

    public Dashboard(Integer id) {
        frame = new JFrame("Dashboard");
        frame.setSize(500, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel greeting;
        if (id != null) {
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
            greeting = new JLabel("HI, " + name + ". Following is your total expense.");
            panel.add(greeting);
            JButton addExpenseButton = new JButton("Add Expense");
            addExpenseButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    new AddExpense(id, null);
                    frame.dispose();
                }
            });
            handleDashboard(id);
            panel.add(addExpenseButton);
            JButton logoutButton = new JButton("Logout");
            logoutButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent actionEvent) {
                    int confirm = DisplayMessage.confirmMessage(frame, "Are you sure to Log out ?",
                            "Logout Confirmation");
                    if (confirm == JOptionPane.YES_OPTION) {
                        DisplayMessage.successMessage(frame, "You Logged Out Successfully !", "Logout Success");
                        new Dashboard(null);
                        frame.dispose();
                    }
                }
            });
            panel.add(logoutButton);
        } else {
            greeting = new JLabel("Hmm, Looks like you are not Logged In!");
            JButton registerButton = new JButton("Register");
            registerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent action) {
                    new Register();
                    frame.dispose();
                }
            });
            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent loginAction) {
                    new Login();
                    frame.dispose();
                }
            });
            panel.add(greeting);
            panel.add(registerButton);
            panel.add(loginButton);
        }
        greeting.setFont(new Font("Arial", Font.BOLD, 18));

        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = DisplayMessage.confirmMessage(frame, "Are you sure to Exit ?",
                        "Exit Confirmation");
                if (confirm == JOptionPane.YES_OPTION) {
                    frame.dispose();
                }
            }
        });
        panel.add(exitButton);
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
