package login;

import common.ChangePasswordUI;
import dao.AdminDAO;
import dao.StudentDAO;
import entity.Admin;
import entity.Student;
import student.StudentUI;
import util.AccountManager;
import util.Constants;
import util.HashUtil;
import util.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ChangeInitPasswordUI extends JFrame {
    private JPanel mainPanel;
    private JPasswordField newPasswordField;
    private JPasswordField retypePasswordField;
    private JButton saveButton;
    private JButton cancelButton;

    public ChangeInitPasswordUI() {
        setTitle("Đổi mật khẩu lần đầu");
        setSize(450, 250);
        setLocationRelativeTo(null);    // center
        setContentPane(mainPanel);
        setVisible(true);

        setupFields();
        setupCancelButton();
        setupSaveButton();
    }

    private void setupFields() {
        newPasswordField.setMargin(Constants.fieldMargin);
        retypePasswordField.setMargin(Constants.fieldMargin);
    }

    private void setupCancelButton() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangeInitPasswordUI.this.setVisible(false);
                ChangeInitPasswordUI.this.dispose();
            }
        });
    }

    private void setupSaveButton() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    char[] newPWChar = newPasswordField.getPassword();
                    char[] retypePWChar = retypePasswordField.getPassword();

                    if (newPWChar.length == 0) throw new Exception("Vui lòng nhập mật khẩu mới.");
                    if (!Arrays.equals(retypePWChar, newPWChar)) throw new Exception("Mật khẩu nhập lại không trùng!");

                    byte[] salt = AccountManager.getUsername().getBytes();
                    Student student = AccountManager.getCurrentStudent();
                    student.setHashedPw(HashUtil.getHash(newPWChar, salt));
                    boolean ok = StudentDAO.update(student);
                    if (!ok) throw new Exception("Lỗi không xác định!");
                    ChangeInitPasswordUI.this.setVisible(false);
                    new StudentUI();
                    ChangeInitPasswordUI.this.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Đổi mật khẩu thất bại", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}
