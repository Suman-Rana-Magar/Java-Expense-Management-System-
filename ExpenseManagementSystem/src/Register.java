import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Register {
    private JFrame frame;
    private JTextField username;
    private JPasswordField password;
    private JPasswordField confirmPassword;

    public Register() {
        frame = new JFrame("User Registration");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        username = new JTextField(20);
        password = new JPasswordField(20);
        confirmPassword = new JPasswordField(20);

        JButton registerButton = new JButton("Register");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = username.getText();
                String pass = new String(password.getPassword());
                String confirmPass = new String(confirmPassword.getPassword());
                if (name.isBlank() || pass.isBlank() || confirmPass.isBlank())
                    DisplayMessage.errorMessage(frame, "Please fill data in each field", "Validation Error");
                else if (!pass.equals(confirmPass))
                    DisplayMessage.errorMessage(frame, "Password and confirm password must be same",
                            "Validation Error");
                else
                    handleRegistration(username.getText(), new String(password.getPassword()));
            }
        });

        // JPanel for auto setBounds
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JLabel title = new JLabel("User Registration");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        panel.add(title);
        panel.add(new JLabel("Username:"));
        panel.add(username);
        panel.add(new JLabel("Password:"));
        panel.add(password);
        panel.add(new JLabel("Confirm Password"));
        panel.add(confirmPassword);
        panel.add(registerButton);
        panel.add(new JLabel("Already have an account?"));
        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Login();
                frame.dispose();
            }
        });
        panel.add(loginButton);

        JButton returnDashboardButton = new JButton("Return to Dashboard");
        returnDashboardButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Dashboard(null);
                frame.dispose();
            }
        });
        panel.add(returnDashboardButton);
        frame.add(panel);

        // hardcoding
        // username.setBounds(100, 100, 100, 20);
        // password.setBounds(100, 130, 100, 20);
        // confirmPassword.setBounds(100, 160, 100, 20);
        // registerButton.setBounds(100, 190, 100, 20);
        // frame.add(username);
        // frame.add(password);
        // frame.add(confirmPassword);
        // frame.add(registerButton);
        // frame.setLayout(null);

        frame.setVisible(true);
    }

    private void handleRegistration(String username, String password) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String selectSql = "select id from users where username=?";
            PreparedStatement selectStmt = con.prepareStatement(selectSql);
            selectStmt.setString(1, username);
            ResultSet user = selectStmt.executeQuery();
            if (user.next())
                DisplayMessage.errorMessage(frame, "Username Already Exist !", "Duplicate Username");
            else {
                String insertSql = "insert into users (username,password) values (?,?)";
                PreparedStatement insertStmt = con.prepareStatement(insertSql);
                insertStmt.setString(1, username);
                insertStmt.setString(2, password);
                insertStmt.executeUpdate();
                DisplayMessage.successMessage(frame, "User Registered Successfully !", "Registration Success");
                new Login();
                frame.dispose();
            }
        } catch (Exception e) {
            DisplayMessage.errorMessage(frame, e.getMessage(), "Exception Occured");
        }
    }
}
