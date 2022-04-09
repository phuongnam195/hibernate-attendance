package entity;

import java.sql.Date;
import java.sql.Time;
import java.util.Objects;

public class Subject {
    private String id;
    private String name;
    private Date startDate;
    private Date finishDate;
    private Integer weekday;
    private Time startTime;
    private Time finishTime;
    private String classroom;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Integer getWeekday() {
        return weekday;
    }

    public void setWeekday(Integer weekday) {
        this.weekday = weekday;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Time finishTime) {
        this.finishTime = finishTime;
    }

    public String getClassroom() {
        return classroom;
    }

    public void setClassroom(String classroom) {
        this.classroom = classroom;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) && Objects.equals(name, subject.name) && Objects.equals(startDate, subject.startDate) && Objects.equals(finishDate, subject.finishDate) && Objects.equals(weekday, subject.weekday) && Objects.equals(startTime, subject.startTime) && Objects.equals(finishTime, subject.finishTime) && Objects.equals(classroom, subject.classroom);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, startDate, finishDate, weekday, startTime, finishTime, classroom);
    }
}
