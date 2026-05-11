package user;

// 모든 사용자(학생, 관리자)의 공통 정보를 담는 추상 클래스
public abstract class User {
    private final String studentId; // 학번
    private final String name;      // 이름

    public User(String studentId, String name) {
        this.studentId = studentId;
        this.name = name;
    }

    public String getStudentId() {
        return studentId;
    }

    public String getName() {
        return name;
    }

    // 사용자 역할 반환
    public abstract String getRole();

    // 분실물 등록 가능 여부
    public abstract boolean canRegisterItem();

    // 분실물 상태 변경 가능 여부
    public abstract boolean canChangeStatus();
}
