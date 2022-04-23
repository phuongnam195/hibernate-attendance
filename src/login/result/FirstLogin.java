package login.result;

import entity.Student;

public class FirstLogin extends LoginResult {
    private final Student student;

    public FirstLogin(Student student) {
        this.student = student;
    }

    public Student getStudent() {
        return student;
    }
}
