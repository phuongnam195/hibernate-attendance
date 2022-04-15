package util;

import entity.Admin;
import entity.Student;

public class Setting {
    private static Student currentStudent;
    private static Admin currentAdmin;

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
}
