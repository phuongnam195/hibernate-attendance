package student;

import dao.LearningDAO;
import dao.SubjectDAO;
import entity.Learning;
import entity.Student;
import entity.Subject;
import util.Constants;
import util.DateTimeUtil;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class ViewAttendanceStudentUI extends JFrame {
    private JPanel mainPanel;
    private JTable table;
    private JButton closeButton;

    private List<Learning> attendances;
    private Student student;

    public ViewAttendanceStudentUI(Student student) {
        this.student = student;

        setTitle("Xem kết quả điểm danh");
        setSize(1200, 600);
        setLocationRelativeTo(null);    // center
        setContentPane(mainPanel);
        setVisible(true);

        setupTable();
        setupCloseButton();
    }

    private void setupTable() {
        List<Object[]> rows = new ArrayList<>();
        attendances = LearningDAO.getListLearningOfStudent(student.getId());
        for (Learning ln : attendances) {
            List<Object> row = new ArrayList<>();
            row.add(ln.getSubjectId());
            Subject subject = SubjectDAO.getById(ln.getSubjectId());
            row.add(subject.getName());
            Date currentDate = DateTimeUtil.currentDate();
            int daysDiff = DateTimeUtil.daysDiff(subject.getStartDate(), currentDate);
            int weekIndex = daysDiff / 7;
            if (daysDiff % 7 == 0) {
                Time currentTime = DateTimeUtil.currentTime();
                Time startTime = subject.getStartTime();
                Time finishTime = subject.getFinishTime();
                if (currentTime.before(startTime) || currentTime.after(finishTime)) {
                    weekIndex--;
                }
            }
            for (int i = 0; i < Constants.maxWeekIndex; i++) {
                if (i <= weekIndex) {
                    boolean isAttend = ln.getAttendAt(i);
                    if (isAttend) {
                        row.add('O');
                    } else {
                        row.add('x');
                    }
                } else {
                    row.add(' ');
                }
            }

            rows.add(row.toArray(new Object[0]));
        }
        Object[][] data = rows.toArray(new Object[0][]);

        List<String> headers = new ArrayList<>();
        headers.add("Mã MH");
        headers.add("Tên môn học");
        for (int i = 1; i <= 15; i++) {
            headers.add("Tuần " + i);
        }
        String[] columnNames = headers.toArray(new String[0]);

        DefaultTableModel tableModel = new DefaultTableModel(data, columnNames);
        table.setModel(tableModel);

        table.getColumnModel().getColumn(0).setPreferredWidth(80);
        table.getColumnModel().getColumn(1).setPreferredWidth(180);
        DefaultTableCellRenderer render = new DefaultTableCellRenderer();
        render.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 2; i < 17; i++) {
            table.getColumnModel().getColumn(i).setPreferredWidth(85);
            table.getColumnModel().getColumn(i).setCellRenderer(render);
        }
    }

    private void setupCloseButton() {
        closeButton.addActionListener(e -> {
            this.setVisible(false);
            this.dispose();
        });
    }
}
