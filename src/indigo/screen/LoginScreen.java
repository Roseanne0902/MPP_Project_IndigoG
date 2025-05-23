package indigo.screen;

import indigo.model.User;
import indigo.model.UserRoleEnum;
import indigo.repository.UserRepository;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;

import static indigo.repository.UserRoleRepository.roleList;

public class LoginScreen extends JFrame {

  private static final long serialVersionUID = 1L;
  private JTextField usernameField;
  private JPasswordField passwordField;
  private JButton loginButton;
  private JPanel contentPane;

  public LoginScreen() {
    setTitle("Expense Tracker Login");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(450, 190, 600, 400);
    setResizable(false);

    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
    setContentPane(contentPane);
    contentPane.setLayout(null);

    JLabel lblTitle = new JLabel("Login");
    lblTitle.setFont(new Font("Times New Roman", Font.BOLD, 36));
    lblTitle.setBounds(240, 30, 120, 40);
    contentPane.add(lblTitle);

    JLabel lblUsername = new JLabel("Username:");
    lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 18));
    lblUsername.setBounds(100, 100, 100, 30);
    contentPane.add(lblUsername);

    usernameField = new JTextField();
    usernameField.setFont(new Font("Tahoma", Font.PLAIN, 18));
    usernameField.setBounds(210, 100, 250, 30);
    contentPane.add(usernameField);

    JLabel lblPassword = new JLabel("Password:");
    lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 18));
    lblPassword.setBounds(100, 150, 100, 30);
    contentPane.add(lblPassword);

    passwordField = new JPasswordField();
    passwordField.setFont(new Font("Tahoma", Font.PLAIN, 18));
    passwordField.setBounds(210, 150, 250, 30);
    contentPane.add(passwordField);

    loginButton = new JButton("Login");
    loginButton.setFont(new Font("Tahoma", Font.PLAIN, 20));
    loginButton.setBounds(230, 220, 120, 40);
    contentPane.add(loginButton);

    this.getRootPane().setDefaultButton(loginButton);

    loginButton.addActionListener((ActionEvent e) -> {
      String username = usernameField.getText();
      String password = new String(passwordField.getPassword());

      if (UserRepository.authenticateUser(username, password)) {
        List<User> users = UserRepository.getUserByUserId(username);
        if (!users.isEmpty()) {
          User user = users.get(0);
          String role = user.getRoleId();
          UserRoleEnum roleEnum = roleList.get(role);
          JOptionPane.showMessageDialog(this, "Login successful! Role: " + roleEnum);
          dispose();
          if (roleEnum == UserRoleEnum.Admin) {
            new AdminDashboard(user).setVisible(true);
          } else {
            new UserDashboard(user).setVisible(true);
          }
        }
      } else {
        JOptionPane.showMessageDialog(this, "Invalid username or password.");
      }
    });
  }
}
