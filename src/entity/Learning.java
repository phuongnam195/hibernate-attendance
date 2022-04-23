package entity;

import util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Learning {
    private int id;
    private String subjectId;
    private String studentId;
    private int attendance;

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

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public void setAttendAt(int weekIndex) {
        attendance |= 1 << weekIndex;
    }

    public boolean getAttendAt(int weekIndex) {
        return ((attendance >> weekIndex) & 1) == 1;
    }

    public boolean isAttendedAt(int weekIndex) {
        int bit = (attendance >> weekIndex) & 1;
        return bit == 1;
    }

    public List<Boolean> getAttendBits() {
        List<Boolean> bits = new ArrayList<>();
        for (int i = 0; i < Constants.maxWeekIndex; i++) {
            Boolean bit = getAttendAt(i);
            bits.add(bit);
        }
        return bits;
    }

    public void setAttendBits(List<Boolean> bits) {
        attendance = 0;
        for (int i = 0; i < bits.size(); i++) {
            attendance  = attendance * 2 + (bits.get(i) ? 1 : 0);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Learning learning = (Learning) o;
        return id == learning.id && attendance == learning.attendance && Objects.equals(subjectId, learning.subjectId) && Objects.equals(studentId, learning.studentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectId, studentId, attendance);
    }
}
