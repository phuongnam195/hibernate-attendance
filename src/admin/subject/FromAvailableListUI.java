package admin.subject;

import dao.LearningDAO;
import entity.Student;
import entity.Subject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class FromAvailableListUI extends JFrame {
    private JList studentsList;
    private JPanel mainPanel;
    private JButton saveButton;
    private JButton cancelButton;
    private JPanel listStudentContainer;
    private JButton selectAllButton;

    private Subject subject;
    private List<Student> students;
    private List<JCheckBox> checkBoxes = new ArrayList<>();
    private boolean isSelectAll = true;

    public FromAvailableListUI(Subject subject) {
        this.subject = subject;
        setTitle("Nhập từ danh sách sẵn (" + subject.getName() + ")");
        setSize(480, 720);
        setLocationRelativeTo(null);    // center
        setContentPane(mainPanel);
        setVisible(true);

        setupCheckBoxList();
        setupSelectAllButton();
        setupCancelButton();
        setupSaveButton();
    }

    private void setupCheckBoxList() {
        listStudentContainer.setLayout(new BoxLayout(listStudentContainer, BoxLayout.PAGE_AXIS));
        students = LearningDAO.getListUnenrolledStudentOfSubject(subject.getId());
        for (Student student : students) {
            JCheckBox row = new JCheckBox(student.getId() + " - " + student.getName());
            row.setMargin(new Insets(10, 10, 10, 10));
            checkBoxes.add(row);
            listStudentContainer.add(row);
        }
    }

    private void setupSelectAllButton() {
        selectAllButton.addActionListener(e -> {
            if (isSelectAll) {
                selectAllButton.setText("Bỏ chọn tất cả");
            } else {
                selectAllButton.setText("Chọn tất cả");
            }
            for (JCheckBox row : checkBoxes) {
                row.setSelected(isSelectAll);
            }
            isSelectAll = !isSelectAll;
        });
    }

    private void setupCancelButton() {
        cancelButton.addActionListener(e -> {
            FromAvailableListUI.this.setVisible(false);
            FromAvailableListUI.this.dispose();
        });
    }

    private void setupSaveButton() {
        saveButton.addActionListener(e -> {
            for (int i = 0; i < checkBoxes.size(); i++) {
                if (checkBoxes.get(i).isSelected()) {
                    LearningDAO.enrollSubject(students.get(i).getId(), subject.getId());
                }
            }
            FromAvailableListUI.this.setVisible(false);
            FromAvailableListUI.this.dispose();
        });
    }
}
