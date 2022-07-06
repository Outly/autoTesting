package reqresApiWithPojo;

public class UserForUpdate {
    private String name;
    private String job;

    public UserForUpdate(String name, String job) {
        this.name = name;
        this.job = job;
    }

    public UserForUpdate() {
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }
}
