package entity;

import java.sql.Date;
import java.util.Objects;

public class Lesson {
    private int id;
    private String subjectId;
    private int weekIndex;
    private Date lessonDate;

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

    public int getWeekIndex() {
        return weekIndex;
    }

    public void setWeekIndex(int weekIndex) {
        this.weekIndex = weekIndex;
    }

    public Date getLessonDate() {
        return lessonDate;
    }

    public void setLessonDate(Date lessonDate) {
        this.lessonDate = lessonDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lesson lesson = (Lesson) o;
        return id == lesson.id && weekIndex == lesson.weekIndex && Objects.equals(subjectId, lesson.subjectId) && Objects.equals(lessonDate, lesson.lessonDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectId, weekIndex, lessonDate);
    }
}
