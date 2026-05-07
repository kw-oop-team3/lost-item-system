
public abstract class User {
    private String studentId;
    private String name;

    public User(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public String getStudentId() { return studentId; }
    public String getName() { return name; }

    public abstract String getRole();
    public abstract boolean canRegisterItem();
    public abstract boolean canChangeStatus();
}
