package dao;

import entity.Subject;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;

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
            System.err.println(e);
        } finally {
            session.close();
        }
        return list;
    }
}
