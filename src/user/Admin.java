
public class Admin extends User {

    public Admin(String studentId, String name) {
        super(studentId, name);
    }

    @Override
    public String getRole() { return null; }

    @Override
    public boolean canRegisterItem() { return false; }

    @Override
    public boolean canChangeStatus() { return false; }
}
