package dao;

import entity.Subject;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;
import util.Logger;

import java.util.List;

public class SubjectDAO {

    public static List<Subject> getListSubject() {
        List<Subject> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select sj from Subject sj";
            Query query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException e) {
            Logger.e("SubjectDAO -> getListSubject()", e);
        } finally {
            session.close();
        }
        return list;
    }

    public static boolean addNewSubject(Subject subject) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.save(subject);
            session.getTransaction().commit();
        } catch (Exception e) {
            Logger.e("SubjectDAO -> addNewSubject()", e);
            session.close();
            return false;
        }
        session.close();
        return true;
    }
}
