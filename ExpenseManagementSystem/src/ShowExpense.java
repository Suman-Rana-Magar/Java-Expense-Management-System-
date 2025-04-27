import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class ShowExpense {
    JFrame frame = new JFrame("Show Expenses");
    JPanel panel = new JPanel();
    JPanel rowPanel = new JPanel(new GridLayout(0, 8));

    public ShowExpense(int userId, String sort, String sortOrder) {
        frame.setSize(900, 500);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        try {
            String sortOption[] = { "Title", "Amount", "Date" };
            JComboBox<String> sortByDropDown = new JComboBox<>(sortOption);
            if (!sort.equals("id")) {
                String preSort;
                if (sort.equalsIgnoreCase("amount"))
                    preSort = "Amount";
                else if (sort == "expense_date")
                    preSort = "Date";
                else
                    preSort = "Title";
                sortByDropDown.setSelectedItem(preSort);
            }
            panel.add(new JLabel("Sort By"));
            panel.add(sortByDropDown);

            String orderOption[] = { "Ascending", "Descending" };
            JComboBox<String> orderByDropDown = new JComboBox<>(orderOption);
            if (!sortOrder.equals("asc"))
                orderByDropDown.setSelectedItem("Descending");
            panel.add(new JLabel("Order"));
            panel.add(orderByDropDown);

            JButton sortButton = new JButton("Sort");
            sortButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    String sortBy = (String) sortByDropDown.getSelectedItem();
                    sortBy = sortBy == "Date" ? "expense_date" : sortBy;
                    String orderBy = (String) orderByDropDown.getSelectedItem() == "Ascending" ? "asc" : "desc";
                    new ShowExpense(userId, sortBy.toLowerCase(), orderBy);
                    frame.dispose();
                }
            });
            panel.add(sortButton);

            Connection con = DatabaseConnection.getConnection();
            String sql = "select * from expenses where user_id=" + userId + " order by " + sort + " " + sortOrder;
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
                            handleDeletion(expId, userId, sort, sortOrder);
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
                    ExportFile.exportToCSV(frame, userId, userName, sort, sortOrder);
                }
            });

            JButton exportToText = new JButton("Export To TXT");
            exportToText.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    ExportFile.exportToText(frame, userId, userName, sort, sortOrder);
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

    private void handleDeletion(int expId, int userId, String sort, String sortOrder) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String sql = "delete from expenses where id=" + expId;
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.executeUpdate();
            DisplayMessage.successMessage(frame, "Expense Deleted Successfully !", "Deletion Success");
            new ShowExpense(userId, sort, sortOrder); // todo
            frame.dispose();
        } catch (Exception exp) {
            DisplayMessage.errorMessage(frame, exp.getMessage(), "Exception");
        }
    }
}
