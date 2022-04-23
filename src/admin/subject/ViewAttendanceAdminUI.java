package admin.subject;

import dao.LearningDAO;
import dao.StudentDAO;
import entity.Learning;
import entity.Student;
import entity.Subject;
import util.Constants;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class ViewAttendanceAdminUI extends JFrame {
    private JTable attendanceTable;
    private JPanel mainPanel;
    private JButton cancelButton;
    private JButton saveButton;

    private Subject subject;
    private AttendanceTableModel attendanceTableModel;
    private List<Learning> learnings;

    public ViewAttendanceAdminUI(Subject subject) {
        this.subject = subject;
        setTitle("Xem điểm danh (" + subject.getName() + ")");
        setSize(1200, 600);
        setLocationRelativeTo(null);    // center
        setContentPane(mainPanel);
        setVisible(true);

        setupTable();
        setupCancelButton();
        setupSaveButton();
    }

    private void setupTable() {
        List<Object[]> rows = new ArrayList<>();
        learnings = LearningDAO.getListLearningOfSubject(subject.getId());
        for (Learning ln : learnings) {
            List<Object> row = new ArrayList<>();
            row.add(ln.getStudentId());
            Student student = StudentDAO.getById(ln.getStudentId());
            row.add(student.getName());
            row.addAll(ln.getAttendBits());
            rows.add(row.toArray(new Object[0]));
        }
        Object[][] data = rows.toArray(new Object[0][]);

        List<String> headers = new ArrayList<>();
        headers.add("MSSV");
        headers.add("Tên sinh viên");
        for (int i = 1; i <= 15; i++) {
            headers.add("Tuần " + i);
        }
        String[] columnNames = headers.toArray(new String[0]);

        attendanceTableModel = new AttendanceTableModel(data, columnNames);
        attendanceTable.setModel(attendanceTableModel);

        attendanceTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        attendanceTable.getColumnModel().getColumn(1).setPreferredWidth(200);
        for (int i = 2; i < 17; i++) {
            attendanceTable.getColumnModel().getColumn(i).setPreferredWidth(85);
        }
    }

    private void setupCancelButton() {
        cancelButton.addActionListener(e -> {
            ViewAttendanceAdminUI.this.setVisible(false);
            ViewAttendanceAdminUI.this.dispose();
        });
    }

    private void setupSaveButton() {
        saveButton.addActionListener(e -> {
            for (int i = 0; i < learnings.size(); i++) {
                List<Boolean> bits = new ArrayList<>();
                for (int j = 2; j < Constants.maxWeekIndex + 2; j++) {
                    bits.add((Boolean) attendanceTableModel.getValueAt(i, j));
                }
                Learning newLearning = learnings.get(i);
                newLearning.setAttendBits(bits);
                    LearningDAO.updateLearning(newLearning);
                ViewAttendanceAdminUI.this.setVisible(false);
                ViewAttendanceAdminUI.this.dispose();
            }
        });
    }

    private class AttendanceTableModel extends DefaultTableModel {
        public AttendanceTableModel(Object[][] data, Object[] columnNames) {
            setDataVector(data, columnNames);
        }

        @Override
        public Class<?> getColumnClass(int columnIndex) {
            if (columnIndex == 0 || columnIndex == 1) {
                return String.class;
            } else {
                return Boolean.class;
            }
        }

        @Override
        public boolean isCellEditable(int row, int column) {
            return column >= 2;
        }

        @Override
        public void setValueAt(Object aValue, int row, int column) {
            if (aValue instanceof Boolean && column >= 2) {
                Vector rowData = (Vector) getDataVector().get(row);
                rowData.set(column, aValue);
                fireTableCellUpdated(row, column);
            }
        }
    }
}
