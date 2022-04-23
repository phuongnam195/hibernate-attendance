package admin.student;

import common.Callable;
import dao.StudentDAO;
import entity.Student;
import util.Constants;

import javax.swing.*;

public class NewStudentUI extends JFrame {
    private JPanel mainPanel;
    private JTextField idField;
    private JTextField nameField;
    private JButton saveButton;

    private Student student;
    private Callable onSave;

    public NewStudentUI(Callable onSave) {
        this.onSave = onSave;
        setTitle("Thêm sinh viên");
        setSize(450, 300);
        setLocationRelativeTo(null);    // center
        setContentPane(mainPanel);
        setVisible(true);

        idField.setMargin(Constants.fieldMargin);
        nameField.setMargin(Constants.fieldMargin);

        setupSaveButton();
    }

    private void setupSaveButton() {
        saveButton.addActionListener(e -> {
            student = new Student();

            try {
                if (idField.getText().isEmpty()) throw new Exception("Không được để MSSV trống!");
                if (nameField.getText().isEmpty()) throw new Exception("Không được để họ tên trống!");

                student.setId(idField.getText());
                student.setName(nameField.getText());

                boolean ok = StudentDAO.addNewStudent(student);
                if (ok) {
                    if (onSave != null) {
                        onSave.call(student.getId());
                    }
                    NewStudentUI.this.setVisible(false);
                    NewStudentUI.this.dispose();
                } else {
                    throw new Exception("Trùng mã số sinh viên!");
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Thêm sinh viên thất bại", JOptionPane.WARNING_MESSAGE);
            }

        });
    }
}
