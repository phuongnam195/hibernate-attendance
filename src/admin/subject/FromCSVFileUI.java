package admin.subject;

import dao.LearningDAO;
import dao.StudentDAO;
import dao.SubjectDAO;
import entity.Learning;
import entity.Student;
import util.Logger;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FromCSVFileUI extends JFrame {
    private JPanel mainPanel;
    private JButton csvButton;
    private JButton importButton;
    private JLabel csvLabel;

    private List<Student> importStudent = new ArrayList<>();
    private List<Learning> importLearning = new ArrayList<>();
    private File selectedFile;

    public FromCSVFileUI() {
        setTitle("Nhập từ file CSV");
        setSize(400, 300);
        setLocationRelativeTo(null);    // center
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setContentPane(mainPanel);
        setVisible(true);

        setupCSVButton();
        setImportButton();
    }

    private void setupCSVButton() {
        csvButton.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("CSV UTF-8 (Comma delimited)", "csv"));
            int result = fileChooser.showOpenDialog(FromCSVFileUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                csvLabel.setText(selectedFile.getName());
            }
        });
    }

    private void setImportButton() {
        importButton.addActionListener(e -> {
            boolean ok = readCSV(selectedFile);
            if (ok) {
                for (Student student : importStudent) {
                    StudentDAO.addNewStudent(student);
                }
                for (Learning learning : importLearning) {
                    LearningDAO.addNewLearning(learning);
                }
                FromCSVFileUI.this.setVisible(false);
                FromCSVFileUI.this.dispose();
            }
        });
    }

    private boolean readCSV(File file) {
        String line;
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            br.readLine();  // ignore header line
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                String subjectId = tokens[0];
                String studentId = tokens[1];
                String studentName = tokens.length == 3 ? tokens[2] : "";
                if (subjectId.isEmpty()) throw new Exception("Tồn tại mã môn học bị trống!");
                if (SubjectDAO.getById(subjectId) == null) throw new Exception("Mã môn học " + subjectId + " không tồn tại!");
                Student student = StudentDAO.getById(studentId);
                if (student != null) {
                    if (!studentName.isEmpty() && !student.getName().equals(studentName)) {
                        throw new Exception("Tên sinh viên khác so với danh sách có sẵn!");
                    }
                } else {
                    if (studentName.isEmpty()) {
                        throw new Exception("Thiếu tên sinh viên cho mã số " + studentId);
                    } else {
                        student = new Student();
                        student.setId(studentId);
                        student.setName(studentName);
                        importStudent.add(student);
                    }
                }
                Learning learning = LearningDAO.getLearning(subjectId, studentId);
                if (learning == null) {
                    learning = new Learning();
                    learning.setSubjectId(subjectId);
                    learning.setStudentId(studentId);
                    learning.setAttendance(0);
                    importLearning.add(learning);
                }
            }
            return true;
        } catch (IOException ex) {
            Logger.e("FromCSVFileUI -> setupTemplateComponents()", ex);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(new JFrame(), ex.getMessage(), "Nhập file thất bại", JOptionPane.WARNING_MESSAGE);
        }
        return false;
    }
}
