package login.result;

import entity.Admin;
import entity.Student;

public class LoginSuccess extends LoginResult {
    private final Student student;
    private final Admin admin;

    public LoginSuccess(Student account) {
        this.student = account;
        this.admin = null;
    }

    public LoginSuccess(Admin admin) {
        this.admin = admin;
        this.student = null;
    }

    public Student getStudent() {
        return student;
    }

    public Admin getAdmin() {
        return admin;
    }
}
