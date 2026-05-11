package user;

// 관리자 사용자 클래스
public class Admin extends User {

    public Admin(String studentId, String name) {
        super(studentId, name);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }

    // 관리자는 분실물 등록 가능
    @Override
    public boolean canRegisterItem() {
        return true;
    }

    // 관리자는 상태 변경 가능
    @Override
    public boolean canChangeStatus() {
        return true;
    }
}
