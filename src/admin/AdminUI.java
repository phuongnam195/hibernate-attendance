package admin;

import admin.subject.CreateSubjectUI;
import dao.SubjectDAO;
import entity.Subject;
import login.LoginUI;
import util.Constants;
import util.Setting;
import util.DateUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class AdminUI extends JFrame {
    private JLabel usernameLabel;
    private JButton changePasswordButton;
    private JButton logoutButton;
    private JPanel mainPanel;
    private JTabbedPane managePanel;
    private JPanel subjectPanel;
    private JTextField searchBar;
    private JButton searchButton;
    private JTable subjectTable;
    private JPanel studentPanel;
    private JButton createSubjectButton;
    private JButton importStudentsButton;
    private JButton showAttendanceButton;
    private JButton refreshButton;

    public AdminUI() {
        setTitle(Constants.appName + " (giáo vụ)");
        setSize(1280, 720);
        setLocationRelativeTo(null);    // center
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);

//        usernameLabel.setText("Tên đăng nhập: " + Setting.getCurrentAdmin().getUsername());

        logoutButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                onLogout();
            }
        });

        setupSubjectTab();
    }

    private void onLogout() {
        Setting.logout();
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

        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fetchSubjectTableData();
            }
        });

        createSubjectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new CreateSubjectUI(AdminUI.this);
            }
        });

        showAttendanceButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println(subjectTable.getSelectedRow());
            }
        } );
    }

    public void fetchSubjectTableData() {
        String[] columnNames = {"Mã môn học", "Tên môn học", "Ngày bắt đầu", "Ngày kết thúc", "Thứ", "Giờ bắt đầu", "Giờ kết thúc", "Phòng học"};
        List<String[]> strSubjects = new ArrayList<>();
        List<Subject> subjects = SubjectDAO.getListSubject();
        for (Subject sj : subjects) {
            String[] row = {sj.getId(), sj.getName(), DateUtils.formatDate(sj.getStartDate()), DateUtils.formatDate(sj.getFinishDate()), DateUtils.formatWeekday(sj.getWeekday()), DateUtils.formatTime(sj.getStartTime()), DateUtils.formatTime(sj.getFinishTime()), sj.getClassroom()};
            strSubjects.add(row);
        }
        String[][] data = strSubjects.toArray(new String[0][]);
        DefaultTableModel subjectTableModel = new DefaultTableModel(data, columnNames);
        subjectTable.setModel(subjectTableModel);
    }
}
