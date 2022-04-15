package dao;

import entity.Learning;
import entity.Student;
import entity.Subject;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import util.HibernateUtil;
import util.Logger;

import java.util.List;

public class LearningDAO {
    public static List<Student> getListStudentOfSubject(String subjectId) {
        List<Student> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select st from Learning ln join Student st on st.id = ln.studentId where ln.subjectId = '" + subjectId + "'";
            Query query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException e) {
            Logger.e("LearningDAO -> getListStudentOfSubject()", e);
        } finally {
            session.close();
        }
        return list;
    }

    public static List<Subject> getListSubjectOfStudent(String studentId) {
        List<Subject> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select sj from Learning ln join Subject sj on sj.id = ln.subjectId where ln.studentId = '" + studentId + "'";
            Query query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException e) {
            Logger.e("LearningDAO -> getListSubjectOfStudent()", e);
        } finally {
            session.close();
        }
        return list;
    }

    public static boolean addNewLearning(Learning learning) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            session.save(learning);
            session.getTransaction().commit();
        } catch (Exception e) {
            Logger.e("LearningDAO -> addNewLearning()", e);
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    // students enroll subjects
    public static boolean addNewLearnings(List<Learning> learnings) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        session.beginTransaction();
        try {
            for (Learning learning : learnings) {
                session.save(learning);
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            Logger.e("LearningDAO -> addNewLearnings()", e);
            return false;
        } finally {
            session.close();
        }
        return true;
    }
}
