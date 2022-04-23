package common;

import dao.AdminDAO;
import dao.StudentDAO;
import entity.Admin;
import entity.Student;
import util.AccountManager;
import util.Constants;
import util.HashUtil;
import util.Logger;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ChangePasswordUI extends JFrame {
    private JPanel mainPanel;
    private JPasswordField newPasswordField;
    private JPasswordField retypePasswordField;
    private JButton saveButton;
    private JButton cancelButton;
    private JPasswordField oldPasswordField;

    public ChangePasswordUI() {
        setTitle("Đổi mật khẩu");
        setSize(450, 300);
        setLocationRelativeTo(null);    // center
        setContentPane(mainPanel);
        setVisible(true);

        setupFields();
        setupCancelButton();
        setupSaveButton();
    }

    private void setupFields() {
        oldPasswordField.setMargin(Constants.fieldMargin);
        newPasswordField.setMargin(Constants.fieldMargin);
        retypePasswordField.setMargin(Constants.fieldMargin);
    }

    private void setupCancelButton() {
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ChangePasswordUI.this.setVisible(false);
                ChangePasswordUI.this.dispose();
            }
        });
    }

    private void setupSaveButton() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    char[] oldPWChar = oldPasswordField.getPassword();
                    char[] newPWChar = newPasswordField.getPassword();
                    char[] retypePWChar = retypePasswordField.getPassword();

                    if (oldPWChar.length == 0) throw new Exception("Vui lòng nhập mật khẩu cũ.");
                    if (newPWChar.length == 0) throw new Exception("Vui lòng nhập mật khẩu mới.");
                    if (!Arrays.equals(retypePWChar, newPWChar)) throw new Exception("Mật khẩu nhập lại không trùng!");

                    byte[] salt = AccountManager.getUsername().getBytes();
                    byte[] realOldHashedPW = AccountManager.getHashedPassword();

                    byte[] oldPWHash = HashUtil.getHash(oldPWChar, salt);
                    if (!Arrays.equals(oldPWHash, realOldHashedPW)) throw new Exception("Mật khẩu cũ không đúng!");
                    if (Arrays.equals(newPWChar, oldPWChar)) throw new Exception("Nhập khẩu mới trùng mật khẩu cũ!");

                    boolean ok;
                    if (AccountManager.isAdminLogged()) {
                        Admin admin = AccountManager.getCurrentAdmin();
                        byte[] newHashedPW = HashUtil.getHash(newPWChar, salt);
                        Logger.d(newHashedPW);
                        admin.setHashedPw(newHashedPW);
                        ok = AdminDAO.update(admin);
                    } else {
                        Student student = AccountManager.getCurrentStudent();
                        student.setHashedPw(HashUtil.getHash(newPWChar, salt));
                        ok = StudentDAO.update(student);
                    }
                    if (!ok) throw new Exception("Lỗi không xác định!");
                    ChangePasswordUI.this.setVisible(false);
                    ChangePasswordUI.this.dispose();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Đổi mật khẩu thất bại", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }
}
