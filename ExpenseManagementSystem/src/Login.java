import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class Login {
    private JFrame frame;
    private JTextField username;
    private JPasswordField password;

    public Login() {
        frame = new JFrame("User Login");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        username = new JTextField(20);
        password = new JPasswordField(20);

        JButton registerButton = new JButton("Login");
        registerButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = username.getText();
                String pass = new String(password.getPassword());
                if (name.isBlank() || pass.isBlank())
                    DisplayMessage.errorMessage(frame, "Please fill data in each field", "Validation Error");
                else
                    handleLogin(username.getText(), new String(password.getPassword()));
            }
        });

        // JPanel for auto setBounds
        JPanel panel = new JPanel(new GridLayout(0, 1));
        JLabel title = new JLabel("User Login");
        title.setFont(new Font("Arial", Font.BOLD, 25));
        panel.add(title);
        panel.add(new JLabel("Username:"));
        panel.add(username);
        panel.add(new JLabel("Password:"));
        panel.add(password);
        panel.add(registerButton);
        panel.add(new JLabel("Don't have an account?"));
        JButton loginButton = new JButton("Register");
        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new Register();
                frame.dispose();
            }
        });
        panel.add(loginButton);

        frame.add(panel);

        frame.setVisible(true);
    }

    private void handleLogin(String userName, String passwordStr) {
        try {
            Connection con = DatabaseConnection.getConnection();
            String selectSql = "select id from users where username=? and password=?";
            PreparedStatement selectStmt = con.prepareStatement(selectSql);
            selectStmt.setString(1, userName);
            selectStmt.setString(2, passwordStr);
            ResultSet user = selectStmt.executeQuery();
            if (user.next()) {
                new Dashboard(user.getInt("id"));
                frame.dispose();
            } else {
                DisplayMessage.errorMessage(frame, "User Not Found !", "Not Found");
            }
        } catch (Exception e) {
            DisplayMessage.errorMessage(frame, e.getMessage(), "Exception Occured");
        }
    }
}
