package student;

import dao.LearningDAO;
import entity.Learning;
import entity.Student;
import entity.Subject;
import util.Constants;
import util.DateTimeUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.util.Map;

public class ConfirmAttendUI extends JFrame {
    private JPanel mainPanel;
    private JTextField subjectField;
    private JTextField dateField;
    private JTextField nowField;
    private JTextField timeField;
    private JButton cancelButton;
    private JButton confirmButton;
    private JTextField studentField;
    private JTextField weekIndexField;

    private Student student;
    private Subject subject;
    private Learning learning;
    private int weekIndex;

    public ConfirmAttendUI(Student student, Subject subject, Learning learning, int weekIndex) {
        this.student = student;
        this.subject = subject;
        this.learning = learning;
        this.weekIndex = weekIndex;

        setTitle("Điểm danh");
        setSize(600, 500);
        setLocationRelativeTo(null);    // center
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);

        setupFields();
        setupCancelButton();
        setupConfirmButton();
    }

    private void setupFields() {
        studentField.setMargin(Constants.fieldMargin);
        subjectField.setMargin(Constants.fieldMargin);
        dateField.setMargin(Constants.fieldMargin);
        weekIndexField.setMargin(Constants.fieldMargin);
        timeField.setMargin(Constants.fieldMargin);
        nowField.setMargin(Constants.fieldMargin);

        studentField.setText(student.getName());
        subjectField.setText(subject.getName());
        Date currentDate = DateTimeUtil.currentDate();
        dateField.setText(DateTimeUtil.formatDate(currentDate));
        weekIndexField.setText(String.valueOf(weekIndex + 1));
        timeField.setText(DateTimeUtil.formatTime(subject.getStartTime()) + " - " + DateTimeUtil.formatTime(subject.getFinishTime()));
        nowField.setText(DateTimeUtil.formatTime(DateTimeUtil.currentTime()));
    }

    private void setupCancelButton() {
        cancelButton.addActionListener(e -> {
            ConfirmAttendUI.this.setVisible(false);
            ConfirmAttendUI.this.dispose();
        });
    }

    private void setupConfirmButton() {
        confirmButton.addActionListener(e -> {
            learning.setAttendAt(weekIndex);
            LearningDAO.updateLearning(learning);
            ConfirmAttendUI.this.setVisible(false);
            ConfirmAttendUI.this.dispose();
        });
    }
}
