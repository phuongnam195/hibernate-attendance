import admin.AdminUI;
import dao.LearningDAO;
import dao.SubjectDAO;
import entity.Student;
import entity.Subject;
import login.LoginUI;
import org.hibernate.SessionFactory;
import student.StudentUI;
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

        new LoginUI();
    }

    private static void getAdminPassword() {
        char[] pass = {'n', 'i', 'm', 'd', 'a'};
        byte[] bytes = HashUtil.getHash(pass, "admin".getBytes());
        Logger.d(bytes);
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
