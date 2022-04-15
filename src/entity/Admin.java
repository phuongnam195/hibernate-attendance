package entity;

import java.util.Arrays;
import java.util.Objects;

public class Admin {
    private String username;
    private byte[] hashedPw;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        Admin admin = (Admin) o;
        return Objects.equals(username, admin.username) && Arrays.equals(hashedPw, admin.hashedPw);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(username);
        result = 31 * result + Arrays.hashCode(hashedPw);
        return result;
    }
}
