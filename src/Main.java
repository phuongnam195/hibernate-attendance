import admin.AdminUI;
import dao.LearningDAO;
import dao.SubjectDAO;
import entity.Learning;
import entity.Student;
import entity.Subject;
import org.hibernate.SessionFactory;
import util.Constants;
import util.HashUtil;
import util.HibernateUtil;
import util.Logger;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        Constants.setFontForAll();
        new Thread() {
            SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
        }.run();
//        LoginUI loginUI = new LoginUI();
        AdminUI adminUI = new AdminUI();
    }

    private static void test1() {
        Subject sj = new Subject();
        sj.setId("MH03");
        sj.setName("Ngữ văn");
        sj.setStartDate(new Date(2022, 3, 9));
        sj.setFinishDate(new Date(2022, 4, 9));
        sj.setWeekday(3);
        sj.setStartTime(new Time(8, 0, 0));
        sj.setFinishTime(new Time(11, 0, 0));
        sj.setClassroom("E.103");
        boolean ok = SubjectDAO.addNewSubject(sj);
        System.out.println(ok);
    }

    private static void test2() {
        Learning l = new Learning();
        l.setStudentId("19120591");
        l.setSubjectId("MH01");
        boolean ok = LearningDAO.addNewLearning(l);
        System.out.println(ok);
    }

    private static void test3() {
        List<Student> students = LearningDAO.getListStudentOfSubject("MH02");
        for (int i = 0; i < students.size(); i++) {
            System.out.println(students.get(i).getName());
        }
    }

    private static void getAdminPassword() {
        char[] pass = {'n', 'i', 'm', 'd', 'a'};
        byte[] bytes = HashUtil.getHash(pass, "admin".getBytes());
        Logger.d(bytes);
        StringBuilder builder = new StringBuilder(bytes.length * 2);
        char[] hexChars = new char[bytes.length * 2];
        char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = HEX_ARRAY[v >>> 4];
            hexChars[j * 2 + 1] = HEX_ARRAY[v & 0x0F];
        }
        Logger.d(new String(hexChars));
    }
}
