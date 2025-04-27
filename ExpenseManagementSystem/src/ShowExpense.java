import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ShowExpense {
    JFrame frame = new JFrame("Show Expenses");
    JPanel panel = new JPanel();
    JPanel rowPanel = new JPanel(new GridLayout(0, 8));

    public ShowExpense(int userId) {
        frame.setSize(900, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from expenses where user_id=" + userId;
            PreparedStatement stmt = con.prepareStatement(sql);
            ResultSet expenses = stmt.executeQuery();

            String userSQl = "select username from users where id=" + userId;
            PreparedStatement userStmt = con.prepareStatement(userSQl);
            ResultSet user = userStmt.executeQuery();
            String userName = user.next() ? user.getString("username") : null;

            JLabel headNo = new JLabel("S.No.");
            headNo.setFont(new Font("Arial", Font.BOLD, 15));
            rowPanel.add(headNo);

            JLabel headTitle = new JLabel("Title");
            headTitle.setFont(new Font("Arial", Font.BOLD, 15));
            rowPanel.add(headTitle);

            JLabel headAmount = new JLabel("Amount");
            headAmount.setFont(new Font("Arial", Font.BOLD, 15));
            rowPanel.add(headAmount);

            JLabel headCategory = new JLabel("Category");
            headCategory.setFont(new Font("Arial", Font.BOLD, 15));
            rowPanel.add(headCategory);

            JLabel headPayment = new JLabel("Payment Mode");
            headPayment.setFont(new Font("Arial", Font.BOLD, 15));
            rowPanel.add(headPayment);

            JLabel headDate = new JLabel("Date");
            headDate.setFont(new Font("Arial", Font.BOLD, 15));
            rowPanel.add(headDate);

            JLabel headEdit = new JLabel("Edit");
            headEdit.setFont(new Font("Arial", Font.BOLD, 15));
            rowPanel.add(headEdit);

            JLabel headDelete = new JLabel("Delete");
            headDelete.setFont(new Font("Arial", Font.BOLD, 15));
            rowPanel.add(headDelete);

            panel.add(rowPanel);

            int sNo = 0;
            while (expenses.next()) {
                sNo++;
                String title = expenses.getString("title");
                double amount = expenses.getDouble("amount");
                String category = expenses.getString("category");
                String paymentMode = expenses.getString("payment_mode");
                Date expDate = expenses.getDate("expense_date");
                int expId = expenses.getInt("id");
                JButton editButton = new JButton("Edit");
                editButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        new AddExpense(userId, expId);
                        frame.dispose();
                    }
                });
                JButton deleteButton = new JButton("Delete");
                deleteButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent ae) {
                        int confirmation = DisplayMessage.confirmMessage(frame,
                                "Are you sure you want to delete this expense?", "Confirm Deletion");
                        if (confirmation == JOptionPane.YES_OPTION) {
                            handleDeletion(expId, userId);
                            frame.dispose();
                        }
                    }
                });
                rowPanel.add(new JLabel(String.valueOf(sNo)));
                rowPanel.add(new JLabel(title));
                rowPanel.add(new JLabel(String.valueOf(amount)));
                rowPanel.add(new JLabel(category));
                rowPanel.add(new JLabel(paymentMode));
                rowPanel.add(new JLabel(expDate.toString()));
                rowPanel.add(editButton);
                rowPanel.add(deleteButton);
                rowPanel.add(editButton);
                rowPanel.add(deleteButton);
            }

            JButton dashboardButton = new JButton("Return to Dashboard");
            dashboardButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    new Dashboard(userId);
                    frame.dispose();
                }
            });

            JButton exportToCsv = new JButton("Export To CSV");
            exportToCsv.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ExportFile.exportToCSV(frame, userId, userName);
                }
            });

            JButton exportToText = new JButton("Export To TXT");
            exportToText.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ExportFile.exportToText(frame, userId, userName);
                }
            });

            panel.add(exportToText);
            panel.add(exportToCsv);
            panel.add(dashboardButton);
            frame.add(panel);
        } catch (Exception exp) {
            DisplayMessage.errorMessage(frame, exp.getMessage(), "Exception");
        }
    }

    private void handleDeletion(int expId, int userId) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "delete from expenses where id=" + expId;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            DisplayMessage.successMessage(frame, "Expense Deleted Successfully !", "Deletion Success");
            new ShowExpense(userId);
            frame.dispose();
        } catch (Exception exp) {
            DisplayMessage.errorMessage(frame, exp.getMessage(), "Exception");
        }
    }
}
