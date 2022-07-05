package api;

import java.util.Objects;

public class SuccessReg {
    private Integer id;
    private String token;

    public SuccessReg(int id, String token) {
        this.id = id;
        this.token = token;
    }

    public SuccessReg() {
    }

    public int getId() {
        return id;
    }

    public String getToken() {
        return token;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SuccessReg that = (SuccessReg) o;
        return Objects.equals(id, that.id) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, token);
    }
}
