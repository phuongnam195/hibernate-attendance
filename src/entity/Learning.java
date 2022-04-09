package entity;

import java.util.Objects;

public class Learning {
    private int id;
    private String subjectId;
    private String studentId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(String subjectId) {
        this.subjectId = subjectId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Learning learning = (Learning) o;
        return id == learning.id && Objects.equals(subjectId, learning.subjectId) && Objects.equals(studentId, learning.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectId, studentId);
    }
}
