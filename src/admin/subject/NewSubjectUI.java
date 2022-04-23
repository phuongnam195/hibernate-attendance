package admin.subject;

import admin.AdminUI;
import dao.SubjectDAO;
import entity.Subject;
import popup.DatePicker;
import util.DateTimeUtil;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Time;
import java.sql.Date;

import util.Constants;

public class NewSubjectUI extends JFrame {

    private JPanel mainPanel;
    private JTextField idField;
    private JTextField nameField;
    private JTextField weekdayField;
    private JTextField classroomField;
    private JButton saveButton;
    private JTextField startDateField;
    private JButton startDateButton;
    private JTextField finishDateField;
    private JButton finishDateButton;
    private JSpinner startTimeHour;
    private JSpinner startTimeMinute;
    private JSpinner finishTimeHour;
    private JSpinner finishTimeMinute;

    private Subject subjectModel;
    private SpinnerNumberModel startHourModel;
    private SpinnerNumberModel startMinuteModel;
    private SpinnerNumberModel finishHourModel;
    private SpinnerNumberModel finishMinuteModel;

    private AdminUI parent;

    private static long ONE_DAY_MS = 24 * 60 * 60 * 1000;

    public NewSubjectUI(AdminUI parent) {
        this.parent = parent;
        setTitle("Tạo môn học");
        setSize(540, 720);
        setLocationRelativeTo(null);    // center
        setContentPane(mainPanel);
        setVisible(true);

        subjectModel = new Subject();

        setupStringFields();
        setupDateFields();
        setupTimeFields();
        setupSaveButton();
    }

    private void setupStringFields() {
        idField.setMargin(Constants.fieldMargin);
        nameField.setMargin(Constants.fieldMargin);
        classroomField.setMargin(Constants.fieldMargin);
    }

    private void setupDateFields() {
        weekdayField.setHorizontalAlignment(JTextField.CENTER);

        startDateField.setHorizontalAlignment(JTextField.CENTER);
        startDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date pickedDate = new DatePicker(NewSubjectUI.this).setPickedDate();
                if (pickedDate == null) {
                    return;
                }
                startDateField.setText(DateTimeUtil.formatDate(pickedDate));
                java.sql.Date sqlDate = DateTimeUtil.utilDateToSqlDate(pickedDate);
                subjectModel.setStartDate(sqlDate);

                long ms = sqlDate.getTime() + ONE_DAY_MS * 7 * 15;
                java.sql.Date fifteenWeeksLater = new java.sql.Date(ms);
                finishDateField.setText(DateTimeUtil.formatDate(fifteenWeeksLater));
                subjectModel.setFinishDate(fifteenWeeksLater);

                int weekday = DateTimeUtil.getWeekDay(pickedDate);
                subjectModel.setWeekday(weekday);
                weekdayField.setText(DateTimeUtil.formatWeekday(weekday));
            }
        });

        finishDateField.setHorizontalAlignment(JTextField.CENTER);
        finishDateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Date pickedDate = new DatePicker(NewSubjectUI.this).setPickedDate();
                if (pickedDate == null) {
                    return;
                }
                finishDateField.setText(DateTimeUtil.formatDate(pickedDate));
                java.sql.Date sqlDate = DateTimeUtil.utilDateToSqlDate(pickedDate);
                subjectModel.setFinishDate(sqlDate);

                long ms = sqlDate.getTime() - ONE_DAY_MS * 7 * 15;
                java.sql.Date fifteenWeeksAgo = new java.sql.Date(ms);
                startDateField.setText(DateTimeUtil.formatDate(fifteenWeeksAgo));
                subjectModel.setStartDate(fifteenWeeksAgo);

                int weekday = DateTimeUtil.getWeekDay(pickedDate);
                subjectModel.setWeekday(weekday);
                weekdayField.setText(DateTimeUtil.formatWeekday(weekday));
            }
        });
    }

    private void setupTimeFields() {
        startHourModel = new SpinnerNumberModel(7, 0, 23, 1);
        startMinuteModel = new SpinnerNumberModel(30, 0, 55, 5);
        startTimeHour.setModel(startHourModel);
        startTimeMinute.setModel(startMinuteModel);

        finishHourModel = new SpinnerNumberModel(10, 0, 23, 1);
        finishMinuteModel = new SpinnerNumberModel(0, 0, 55, 5);
        finishTimeHour.setModel(finishHourModel);
        finishTimeMinute.setModel(finishMinuteModel);
    }

    private void setupSaveButton() {
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    if (idField.getText().isEmpty()) throw new Exception("Không được để ID trống!");
                    if (nameField.getText().isEmpty()) throw new Exception("Không được để tên trống!");
                    if (subjectModel.getStartDate() == null) throw new Exception("Không được để ngày bắt đầu trống!");
                    if (subjectModel.getFinishDate() == null) throw new Exception("Không được để ngày kết thúc trống!");
                    if (classroomField.getText().isEmpty()) throw new Exception("Không được để phòng học trống!");

                    subjectModel.setId(idField.getText());
                    subjectModel.setName(nameField.getText());
                    subjectModel.setStartTime(new Time(startHourModel.getNumber().intValue(), startMinuteModel.getNumber().intValue(), 0));
                    subjectModel.setFinishTime(new Time(finishHourModel.getNumber().intValue(), finishMinuteModel.getNumber().intValue(), 0));
                    subjectModel.setClassroom(classroomField.getText());

                    boolean ok = SubjectDAO.addNewSubject(subjectModel);
                    if (ok) {
                        NewSubjectUI.this.setVisible(false);
                        NewSubjectUI.this.dispose();
                        parent.fetchSubjectTableData();
                    } else {
                        throw new Exception("Trùng ID môn học!");
                    }
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Tạo môn thất bại", JOptionPane.WARNING_MESSAGE);
                }
            }
        });
    }

}
