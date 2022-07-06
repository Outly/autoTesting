package reqresApiWithPojo;

public class UserForUpdateWithDate {
    public String name;
    public String job;
    public String updatedAt;

    public UserForUpdateWithDate(String name, String job, String updatedAt) {
        this.name = name;
        this.job = job;
        this.updatedAt = updatedAt;
    }

    public UserForUpdateWithDate() {
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }
}
