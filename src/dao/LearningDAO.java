package dao;

import entity.Learning;
import entity.Student;
import entity.Subject;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import util.HibernateUtil;
import util.Logger;

import java.util.List;

public class LearningDAO {
    public static List<Student> getListStudentOfSubject(String subjectId) {
        List<Student> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select st.name " +
                    "from Learning ln join Student st on st.id = ln.studentId " +
                    "where ln.subjectId = '" + subjectId + "'";
            Query query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException e) {
            Logger.e("LearningDAO -> getListStudentOfSubject()", e);
        } finally {
            session.close();
        }
        return list;
    }

    public static List<Student> getListUnenrolledStudentOfSubject(String subjectId) {
        List<Student> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select st " +
                    "from Student st " +
                    "where not exists (" +
                    "select studentId " +
                    "from Learning " +
                    "where subjectId = '" + subjectId + "' and studentId = st.id)";
            Query query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException e) {
            Logger.e("LearningDAO -> getListUnsignedStudentOfSubject()", e);
        } finally {
            session.close();
        }
        return list;
    }

    public static List<Subject> getListSubjectOfStudent(String studentId) {
        List<Subject> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select sj " +
                    "from Learning ln join Subject sj on sj.id = ln.subjectId " +
                    "where ln.studentId = '" + studentId + "'";
            Query query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException e) {
            Logger.e("LearningDAO -> getListSubjectOfStudent()", e);
        } finally {
            session.close();
        }
        return list;
    }

    public static boolean enrollSubject(String studentId, String subjectId) {
        Learning learning = new Learning();
        learning.setSubjectId(subjectId);
        learning.setStudentId(studentId);
        learning.setAttendance(0);
        return addNewLearning(learning);
    }

    // 1 student enroll 1 subject
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

    // many students enroll many subjects
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

    public static List<Learning> getListLearningOfSubject(String subjectId) {
        List<Learning> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select ln " +
                    "from Learning ln " +
                    "where ln.subjectId = '" + subjectId + "'";
            Query query = session.createQuery(hql);
            list = query.getResultList();
        } catch (HibernateException e) {
            Logger.e("LearningDAO -> getListLearningOfSubject()", e);
        } finally {
            session.close();
        }
        return list;
    }

    public static List<Learning> getListLearningOfStudent(String studentId) {
        List<Learning> list = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select ln " +
                    "from Learning ln " +
                    "where ln.studentId = '" + studentId + "'";
            Query query = session.createQuery(hql);
            list = query.list();
        } catch (HibernateException e) {
            Logger.e("LearningDAO -> getListLearningOfStudent()", e);
        } finally {
            session.close();
        }
        return list;
    }

    public static boolean checkAttendance(String subjectId, String studentId, int weekIndex) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select ln " +
                    "from Learning ln " +
                    "where ln.subjectId = '" + subjectId + "' and ln.studentId = '" + studentId + "'";
            Query query = session.createQuery(hql);
            Learning learning = (Learning) (query.list().get(0));
            return learning.isAttendedAt(weekIndex);
        } catch (HibernateException e) {
            Logger.e("LearningDAO -> getListAttendanceOfSubject()", e);
        } finally {
            session.close();
        }
        return false;
    }

    public static Learning getLearning(String subjectId, String studentId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String hql = "select ln " +
                    "from Learning ln " +
                    "where ln.studentId = '" + studentId + "' and ln.subjectId = '" + subjectId + "'";
            Query query = session.createQuery(hql);
            if (query.list().isEmpty()) return null;
            return (Learning) query.list().get(0);
        } catch (Exception e) {
            Logger.e("LearningDAO -> getLearning()", e);
        } finally {
            session.close();
        }
        return null;
    }

    public static boolean updateLearning(Learning learning) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction transaction = session.beginTransaction();
        try {
            session.update(learning);
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            Logger.e("AdminDAO -> updateLearning()", e);
        } finally {
            session.close();
        }
        return false;
    }
}
