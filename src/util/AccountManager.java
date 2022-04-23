package util;

import entity.Admin;
import entity.Student;

public class AccountManager {
    private static Student currentStudent;
    private static Admin currentAdmin;

    public static boolean isStudentLogged() {
        return currentStudent != null;
    }

    public static boolean isAdminLogged() {
        return currentAdmin != null;
    }

    public static Student getCurrentStudent() {
        return currentStudent;
    }

    public static Admin getCurrentAdmin() {
        return currentAdmin;
    }

    public static void loginAsStudent(Student student) {
        currentStudent = student;
    }

    public static void loginAsAdmin(Admin admin) {
        currentAdmin = admin;
    }

    public static void logout() {
        currentStudent = null;
        currentAdmin = null;
    }

    public static String getUsername() {
        if (currentStudent != null) {
            return currentStudent.getId();
        } else if (currentAdmin != null) {
            return currentAdmin.getUsername();
        }
        return null;
    }

    public static byte[] getHashedPassword() {
        if (currentStudent != null) {
            return currentStudent.getHashedPw();
        } else if (currentAdmin != null) {
            return currentAdmin.getHashedPw();
        }
        return null;
    }
}
