
public class Student extends User {

    public Student(String studentId, String name) {
        super(studentId, name);
    }

    @Override
    public String getRole() { return null; }

    @Override
    public boolean canRegisterItem() { return false; }

    @Override
    public boolean canChangeStatus() { return false; }
}
