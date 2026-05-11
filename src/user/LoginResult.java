package user;

// 로그인 결과를 전달하는 클래스
// 성공하면 user에 값이 들어가고, 실패하면 message에 이유가 들어감
public class LoginResult {
    private final User user;
    private final String message;

    public LoginResult(User user, String message) {
        this.user = user;
        this.message = message;
    }

    // user가 있으면 로그인 성공
    public boolean isSuccess() {
        return user != null;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }
}
