package student;

import common.ChangePasswordUI;
import dao.LearningDAO;
import entity.Learning;
import entity.Subject;
import login.LoginUI;
import util.AccountManager;
import util.Constants;
import util.DateTimeUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StudentUI extends JFrame {
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JTable subjectTable;
    private JButton attendButton;
    private JButton viewAttendanceButton;
    private JButton changePasswordButton;
    private JButton logoutButton;
    private JButton refreshButton;
    private JLabel nameLabel;

    private List<Subject> subjects;
    private DefaultTableModel subjectTableModel;

    public StudentUI() {
        setTitle(Constants.appName + " (sinh viên)");
        setSize(1280, 720);
        setLocationRelativeTo(null);    // center
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);

        setupInfoBar();
        setupSubjectTab();
    }

    private void setupInfoBar() {
        nameLabel.setText("Họ tên: " + AccountManager.getCurrentStudent().getName() + " (" + AccountManager.getCurrentStudent().getId() + ")");

        changePasswordButton.addActionListener(e -> new ChangePasswordUI());

        logoutButton.addActionListener(e -> onLogout());
    }

    private void setupSubjectTab() {
        fetchSubjectTableData();
        subjectTable.setBounds(30, 40, 200, 300);
        subjectTable.setAutoCreateRowSorter(true);
        subjectTable.setDefaultEditor(Object.class, null);  // disable edit cell

        subjectTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setupSubjectTabButtons();
    }

    private void setupSubjectTabButtons() {
        refreshButton.addActionListener(e -> fetchSubjectTableData());

        attendButton.addActionListener(e -> onAttend());

        viewAttendanceButton.addActionListener(e -> onViewAttendance());
    }

    private void fetchSubjectTableData() {
        String[] columnNames = {"Mã MH", "Tên môn học", "Ngày bắt đầu", "Ngày kết thúc", "Thứ", "Giờ bắt đầu", "Giờ kết thúc", "Phòng học"};
        List<String[]> strSubjects = new ArrayList<>();
        subjects = LearningDAO.getListSubjectOfStudent(AccountManager.getCurrentStudent().getId());
        for (Subject sj : subjects) {
            String[] row = {sj.getId(), sj.getName(), DateTimeUtil.formatDate(sj.getStartDate()), DateTimeUtil.formatDate(sj.getFinishDate()), DateTimeUtil.formatWeekday(sj.getWeekday()), DateTimeUtil.formatTime(sj.getStartTime()), DateTimeUtil.formatTime(sj.getFinishTime()), sj.getClassroom()};
            strSubjects.add(row);
        }
        String[][] data = strSubjects.toArray(new String[0][]);
        subjectTableModel = new DefaultTableModel(data, columnNames);
        subjectTable.setModel(subjectTableModel);
        subjectTable.getColumnModel().getColumn(0).setPreferredWidth(65);
        subjectTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        subjectTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        subjectTable.getColumnModel().getColumn(3).setPreferredWidth(120);
        subjectTable.getColumnModel().getColumn(4).setPreferredWidth(50);
    }

    private void onLogout() {
        AccountManager.logout();
        this.setVisible(false);
        new LoginUI();
        this.dispose();
    }

    private Subject getSelectedSubject() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) return null;
        String subjectId = (String) subjectTableModel.getValueAt(selectedRow, 0);
        for (Subject sb : subjects) {
            if (sb.getId().equals(subjectId)) {
                return sb;
            }
        }
        return null;
    }

    private void onAttend() {
        Subject subject = getSelectedSubject();
        if (subject == null) return;

        try {
            Date startDate = subject.getStartDate();
            Date finishDate = subject.getFinishDate();
            int weekday = subject.getWeekday();
            Date currentDate = DateTimeUtil.currentDate();
            int currentWeekday = DateTimeUtil.getWeekDay(currentDate);
            if (currentDate.before(startDate)) throw new Exception("Môn học chưa bắt đầu!");
            if (currentDate.after(finishDate)) throw new Exception("Môn học đã kết thúc");
            if (currentWeekday != weekday)  throw new Exception("Không đúng ngày đi học!");

            Time startTime = subject.getStartTime();
            Time finishTime = subject.getFinishTime();
            Time currentTime = DateTimeUtil.currentTime();
            if (currentTime.after(finishTime)) {
                throw new Exception("Buổi học hôm nay đã kết thúc!");
            } else if (currentTime.before(startTime)) {
                throw new Exception("Buổi học chưa bắt đầu!");
            }

            int weekIndex = DateTimeUtil.daysDiff(subject.getStartDate(), currentDate) / 7;
            Learning learning = LearningDAO.getLearning(subject.getId(), AccountManager.getCurrentStudent().getId());
            if (learning.isAttendedAt(weekIndex)) {
                throw new Exception("Bạn đã điểm danh buổi học hôm nay.");
            }

            new ConfirmAttendUI(AccountManager.getCurrentStudent(), subject, learning, weekIndex);

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Điểm danh thất bại", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void onViewAttendance() {
        new ViewAttendanceStudentUI(AccountManager.getCurrentStudent());
    }
}
