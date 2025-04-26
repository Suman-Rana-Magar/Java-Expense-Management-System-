import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

public class AddExpense {
    private JFrame frame;
    private JPanel panel = new JPanel(new GridLayout(0, 1));

    public AddExpense(int userId, Integer expId) {
        frame = new JFrame(expId != null ? "Edit Expense" : "Add Expense");
        String eTitle = null, eCategory = null, ePayment = null;
        Double eAmount = null;
        Date eDate = null;
        if (expId != null) {
            try {
                Connection con = DatabaseConnection.getConnection();
                String sql = "select * from expenses where id=" + expId;
                PreparedStatement stmt = con.prepareStatement(sql);
                ResultSet expense = stmt.executeQuery();
                while (expense.next()) {
                    eTitle = expense.getString("title");
                    eCategory = expense.getString("category");
                    ePayment = expense.getString("payment_mode");
                    eAmount = expense.getDouble("amount");
                    eDate = expense.getDate("expense_date");
                }
            } catch (Exception exp) {
                DisplayMessage.errorMessage(frame, exp.getMessage(), "Exception Occured");
            }
        }
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JLabel header = new JLabel(expId != null ? "Edit Expense" : "Add Expense");
        panel.add(header);
        header.setFont(new Font("Arial", Font.BOLD, 18));

        panel.add(new JLabel("Title"));
        JTextField title = new JTextField(eTitle != null ? eTitle : "", 20);
        panel.add(title);

        panel.add(new JLabel("Amount"));
        JTextField amount = new JTextField(eAmount != null ? String.valueOf(eAmount) : "", 20);
        panel.add(amount);

        String categoryOptions[] = { "Food", "Transport", "Bills", "Other" };
        JComboBox<String> categoryDropDown = new JComboBox<>(categoryOptions);
        if (eCategory != null)
            categoryDropDown.setSelectedItem(eCategory);
        panel.add(new JLabel("Category"));
        panel.add(categoryDropDown);

        String paymentOptions[] = { "Cash", "Credit Card", "Online Payment", "Bank Transfer", "Mobile Wallet" };
        JComboBox<String> paymentDropdown = new JComboBox<>(paymentOptions);
        if (ePayment != null)
            paymentDropdown.setSelectedItem(ePayment);
        panel.add(new JLabel("Payment Mode"));
        panel.add(paymentDropdown);

        panel.add(new JLabel("Expense Date"));
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        JTextField expenseDate;
        if (eDate != null) {
            String eDateString = sdf.format(eDate);
            expenseDate = new JTextField(eDateString, 15);
        } else {
            expenseDate = new JTextField(sdf.format(new Date()), 15);
        }
        panel.add(expenseDate);

        JButton addButton = new JButton(expId != null ? "Update Expense" : "Add Expense");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String ttl = title.getText();
                String inputAmount = amount.getText();
                String category = (String) categoryDropDown.getSelectedItem();
                String payment = (String) paymentDropdown.getSelectedItem();
                String inputDate = expenseDate.getText();
                double amt = 0;

                if (ttl.isBlank() || inputAmount.isBlank() || category.isBlank() || payment.isBlank()
                        || inputDate.isBlank())
                    DisplayMessage.errorMessage(frame, "Please Fill Each Field !", "Validation Error");
                else {
                    try {
                        amt = Double.parseDouble(inputAmount);
                        if (amt <= 0)
                            DisplayMessage.errorMessage(frame, "Amount must be greater than 0", "Invalid Number");
                        else
                            HandleAddExpense(userId, ttl, amt, category, payment, inputDate.toString(), expId);
                    } catch (Exception exception) {
                        DisplayMessage.errorMessage(frame, "Enter Valid Number", "Invalid Number");
                    }
                }
            }
        });
        panel.add(addButton);
        JButton dashboardButton = new JButton(expId != null ? "Cancel" : "Return To Dashboard");
        dashboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (expId != null)
                    new ShowExpense(userId);
                else
                    new Dashboard(userId);
                frame.dispose();
            }
        });
        panel.add(dashboardButton);
        frame.add(panel);
        frame.setVisible(true);
    }

    private void HandleAddExpense(int userId, String title, double amount, String category, String payment,
            String date, Integer expId) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            Date formattedDate = format.parse(date);
            java.sql.Date expenseDate = new java.sql.Date(formattedDate.getTime());
            Connection con = DatabaseConnection.getConnection();
            String sql = null;
            if (expId != null)
                sql = "update expenses set title=?,amount=?,category=?,payment_mode=?,expense_date=?,user_id=? where id="
                        + expId;
            else
                sql = "insert into expenses (title,amount,category,payment_mode,expense_date,user_id) values (?,?,?,?,?,?)";
            PreparedStatement pstmt = con.prepareStatement(sql);
            pstmt.setString(1, title);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, category);
            pstmt.setString(4, payment);
            pstmt.setDate(5, expenseDate);
            pstmt.setInt(6, userId);
            pstmt.executeUpdate();
            if (expId != null) {
                DisplayMessage.successMessage(frame, "Expense Updated Successfully !", "Expense Updated");
                new ShowExpense(userId);
                frame.dispose();
            } else {
                DisplayMessage.successMessage(frame, "Expense Stored Successfully !", "Expense Stored");
                new AddExpense(userId, null);
                frame.dispose();
            }
        } catch (Exception e) {
            DisplayMessage.errorMessage(frame, e.getMessage(), "Exception Occured");
        }
    }
}