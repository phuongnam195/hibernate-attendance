package admin;

import admin.student.NewStudentUI;
import admin.subject.FromCSVFileUI;
import admin.subject.NewSubjectUI;
import admin.subject.ViewAttendanceAdminUI;
import admin.subject.FromAvailableListUI;
import common.Callable;
import common.ChangePasswordUI;
import dao.LearningDAO;
import dao.StudentDAO;
import dao.SubjectDAO;
import entity.Student;
import entity.Subject;
import login.LoginUI;
import util.Constants;
import util.AccountManager;
import util.DateTimeUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class AdminUI extends JFrame {
    private JLabel usernameLabel;
    private JButton changePasswordButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JTabbedPane tabbedPane;
    private JPanel subjectPanel;
    private JTable subjectTable;
    private JPanel studentPanel;
    private JButton createSubjectButton;
    private JButton importStudentsButton;
    private JButton showAttendanceButton;
    private JButton refreshButton;
    private JTable studentTable;
    private JButton refresh2Button;
    private JButton addStudentButton;

    private List<Subject> subjects;
    private List<Student> students;
    private DefaultTableModel subjectTableModel;
    private DefaultTableModel studentTableModel;

    public AdminUI() {
        setTitle(Constants.appName + " (giáo vụ)");
        setSize(1280, 720);
        setLocationRelativeTo(null);    // center
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);

        setupInfoBar();
        setupSubjectTab();
        setupStudentTab();
    }

    private void setupInfoBar() {
        usernameLabel.setText("Tên đăng nhập: " + AccountManager.getCurrentAdmin().getUsername());

        changePasswordButton.addActionListener(e -> new ChangePasswordUI());
        logoutButton.addActionListener(e -> onLogout());
    }

    private void onLogout() {
        AccountManager.logout();
        this.setVisible(false);
        new LoginUI();
        this.dispose();
    }

    private void setupSubjectTab() {
        fetchSubjectTableData();
        subjectTable.setBounds(30, 40, 200, 300);
        subjectTable.setAutoCreateRowSorter(true);
        subjectTable.setDefaultEditor(Object.class, null);  // disable edit cell

        subjectTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        setupSubjectTabButtons();
    }

    private void setupStudentTab() {
        fetchStudentTableData();
        studentTable.setBounds(30, 40, 200, 300);
        studentTable.setAutoCreateRowSorter(true);
        studentTable.setDefaultEditor(Object.class, null);  // disable edit cell

        setupStudentTabButtons();
    }

    private void setupSubjectTabButtons() {
        refreshButton.addActionListener(e -> fetchSubjectTableData());

        createSubjectButton.addActionListener(e -> new NewSubjectUI(AdminUI.this));

        importStudentsButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JPopupMenu importStdMenu = new JPopupMenu();

                importStdMenu.add(new JMenuItem(new AbstractAction("Từ file CSV") {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        new FromCSVFileUI();
                    }
                }));
                if (subjectTable.getSelectedRow() != -1) {
                    importStdMenu.add(new JMenuItem(new AbstractAction("Từ danh sách sẵn") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            Subject selectedSubject = getSelectedSubject();
                            if (selectedSubject != null) {
                                new FromAvailableListUI(selectedSubject);
                            }
                        }
                    }), 0);
                    importStdMenu.add(new JMenuItem(new AbstractAction("Sinh viên mới") {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            new NewStudentUI(new AddStudentToSubject());
                        }
                    }), 1);
                }
                importStdMenu.show(e.getComponent(), e.getX(), e.getY());
            }
        });

        showAttendanceButton.addActionListener(e -> {
            Subject selectedSubject = getSelectedSubject();
            if (selectedSubject != null) {
                new ViewAttendanceAdminUI(selectedSubject);
            }
        });
    }

    private void setupStudentTabButtons() {
        refresh2Button.addActionListener(e -> fetchStudentTableData());

        addStudentButton.addActionListener(e -> {
            new NewStudentUI(new RefreshAfterAdded());
        });
    }

    public void fetchSubjectTableData() {
        String[] columnNames = {"Mã MH", "Tên môn học", "Ngày bắt đầu", "Ngày kết thúc", "Thứ", "Giờ bắt đầu", "Giờ kết thúc", "Phòng học"};
        List<String[]> strSubjects = new ArrayList<>();
        subjects = SubjectDAO.getListSubject();
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

    public void fetchStudentTableData() {
        String[] columnNames = {"Mã sinh viên", "Tên sinh viên"};
        List<String[]> strStudents = new ArrayList<>();
        students = StudentDAO.getListStudent();
        for (Student st : students) {
            String[] row = { st.getId(), st.getName() };
            strStudents.add(row);
        }
        String[][] data = strStudents.toArray(new String[0][]);
        studentTableModel = new DefaultTableModel(data, columnNames);
        studentTable.setModel(studentTableModel);
        studentTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        studentTable.getColumnModel().getColumn(1).setPreferredWidth(100);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        studentTable.getColumnModel().getColumn(0).setCellRenderer(render);
        studentTable.getColumnModel().getColumn(1).setCellRenderer(render);
    }

    public class AddStudentToSubject implements Callable {
        public void call(Object... args) {
            fetchStudentTableData();

            String studentId = (String) args[0];
            boolean ok = LearningDAO.enrollSubject(studentId, getSelectedSubjectId());
            if (!ok) {
                JOptionPane.showMessageDialog(new JFrame(), "Lỗi không xác định", "Thêm sinh viên thất bại", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public class RefreshAfterAdded implements Callable {
        public void call(Object... args) {
            fetchStudentTableData();
        }
    }

    private String getSelectedSubjectId() {
        int selectedRow = subjectTable.getSelectedRow();
        if (selectedRow == -1) return null;
        String subjectId = (String) subjectTableModel.getValueAt(selectedRow, 0);
        return subjectId;
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
}
