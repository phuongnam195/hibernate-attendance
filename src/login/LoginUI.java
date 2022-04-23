package login;

import admin.AdminUI;
import dao.AdminDAO;
import dao.StudentDAO;
import login.result.FirstLogin;
import login.result.LoginFailed;
import login.result.LoginResult;
import login.result.LoginSuccess;
import student.StudentUI;
import util.Constants;
import util.AccountManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginUI extends JFrame {
    private JPanel mainPanel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel usernameLabel;
    private JLabel passwordLabel;

    public LoginUI() {
        setTitle(Constants.appName);
        setSize(720, 540);
        setLocationRelativeTo(null);    // center
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);
        usernameField.setMargin(new Insets(10, 10, 10, 10));
        passwordField.setMargin(new Insets(10, 10, 10, 10));
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLogin();
            }
        });
    }

    private void onLogin() {
        String username = usernameField.getText();
        char[] password = passwordField.getPassword();
        if (!onLoginAsAdmin(username, password)) {
            onLoginAsStudent(username, password);
        }
    }

    private boolean onLoginAsAdmin(String username, char[] password) {
        LoginResult result = AdminDAO.checkLogin(username, password);
        if (result instanceof LoginSuccess) {
            LoginSuccess success = (LoginSuccess) result;
            AccountManager.loginAsAdmin(success.getAdmin());
            this.setVisible(false);
            new AdminUI();
            this.dispose();
        } else if (result instanceof LoginFailed) {
            LoginFailed failed = (LoginFailed) result;
            if (failed.getCode() == LoginFailed.WRONG_PASSWORD) {
                JLabel message = new JLabel(failed.getString());
                message.setFont(Constants.appFont);
                JOptionPane.showMessageDialog(new JFrame(), message, "Đăng nhập thất bại", JOptionPane.WARNING_MESSAGE);
            } else {
                return false;
            }
        }
        return true;
    }

    private void onLoginAsStudent(String username, char[] password) {
        LoginResult result = StudentDAO.checkLogin(username, password);
        if (result instanceof LoginSuccess) {
            LoginSuccess success = (LoginSuccess) result;
            AccountManager.loginAsStudent(success.getStudent());
            this.setVisible(false);
            new StudentUI();
            this.dispose();
        } else if (result instanceof LoginFailed) {
            LoginFailed failed = (LoginFailed) result;
            JLabel message = new JLabel(failed.getString());
            message.setFont(Constants.appFont);
            JOptionPane.showMessageDialog(new JFrame(), message, "Đăng nhập thất bại", JOptionPane.WARNING_MESSAGE);
        } else if (result instanceof FirstLogin) {
            FirstLogin success = (FirstLogin) result;
            AccountManager.loginAsStudent(success.getStudent());
            new ChangeInitPasswordUI();
        }
    }

}
