package dao;

import entity.Admin;
import login.result.LoginFailed;
import login.result.LoginResult;
import login.result.LoginSuccess;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HashUtil;
import util.HibernateUtil;
import util.Logger;

import java.util.Arrays;

public class AdminDAO {
    public static LoginResult checkLogin(String username, char[] password) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        String hql = "select ad from Admin ad where ad.username = '" + username + "'";
        Query query = session.createQuery(hql);
        // Không tìm thấy admin
        if (query.list().isEmpty()) {
            return new LoginFailed(LoginFailed.USERNAME_NOT_EXISTS);
        }
        // Tìm thấy admin
        else {
            Admin admin = (Admin) query.list().get(0);
            byte[] realHashedPW = admin.getHashedPw();
            byte[] testHashedPW = HashUtil.getHash(password, username.getBytes());
            if (Arrays.equals(testHashedPW, realHashedPW)) {
                return new LoginSuccess(admin);
            } else {
                return new LoginFailed(LoginFailed.WRONG_PASSWORD);
            }
        }
    }

    public static boolean update(Admin admin) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(admin);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            Logger.e("AdminDAO -> update()", e);
        } finally {
            session.close();
        }
        return false;
    }
}
