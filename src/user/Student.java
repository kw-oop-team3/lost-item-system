package user;

// 학생 사용자 클래스
public class Student extends User {

    public Student(String studentId, String name) {
        super(studentId, name);
    }

    @Override
    public String getRole() {
        return "STUDENT";
    }

    // 학생은 분실물 등록 불가
    @Override
    public boolean canRegisterItem() {
        return false;
    }

    // 학생은 상태 변경 불가
    @Override
    public boolean canChangeStatus() {
        return false;
    }
}
