package dao;

import entity.Student;
import login.result.FirstLogin;
import login.result.LoginFailed;
import login.result.LoginResult;
import login.result.LoginSuccess;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HashUtil;
import util.HibernateUtil;
import util.Logger;

import java.util.Arrays;
import java.util.List;

public class StudentDAO {

    public static LoginResult checkLogin(String username, char[] password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "select st from Student st where st.id = '" + username + "'";
        Query query = session.createQuery(hql);
        // Không tìm thấy học sinh
        if (query.list().isEmpty()) {
            return new LoginFailed(LoginFailed.USERNAME_NOT_EXISTS);
        } else {
            Student student = (Student) query.list().get(0);

            // Học sinh này chưa có mật khẩu
            if (student.getHashedPw() == null) {
                // Mật khẩu nhập vào là MSSV
                if (password.toString().equals(username)) {
                    return new FirstLogin();
                }
                // Mật khẩu nhập vào không phải MSSV
                else {
                    return new LoginFailed(LoginFailed.WRONG_PASSWORD);
                }
            }
            // Học sinh này đã có mật khẩu
            else {
                byte[] realHashedPW = student.getHashedPw();
                byte[] testHashedPW = HashUtil.getHash(password, username.getBytes());
                // Mật khẩu nhập vào giống mật khẩu được lưu trữ
                if (Arrays.equals(testHashedPW, realHashedPW)) {
                    return new LoginSuccess(student);
                }
                // Sai mật khẩu
                return new LoginFailed(LoginFailed.WRONG_PASSWORD);
            }
        }
    }

    public static List<Student> getListStudent() {
        List<Student> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select st from Student st";
            Query query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException e) {
            Logger.e("StudentDAO -> getListStudent()", e);
        } finally {
            session.close();
        }
        return list;
    }
}
