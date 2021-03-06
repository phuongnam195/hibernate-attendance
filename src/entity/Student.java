package entity;

import java.util.Arrays;
import java.util.Objects;

public class Student {
    private String id;
    private String name;
    private byte[] hashedPw;

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

    public byte[] getHashedPw() {
        return hashedPw;
    }

    public void setHashedPw(byte[] hashedPw) {
        this.hashedPw = hashedPw;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(name, student.name) && Arrays.equals(hashedPw, student.hashedPw);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, name);
        result = 31 * result + Arrays.hashCode(hashedPw);
        return result;
    }
}
