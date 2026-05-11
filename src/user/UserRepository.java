package user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class UserRepository {
    public static final String RESULT_SUCCESS = "SUCCESS";
    public static final String RESULT_DUPLICATE = "DUPLICATE";
    public static final String RESULT_ERROR = "ERROR";
    public static final String RESULT_INVALID = "INVALID";
    public static final String RESULT_ADMIN_RESERVED = "ADMIN_RESERVED";

    private static final String ADMIN_ID = "0000";
    private static final String ADMIN_NAME = "관리자";

    // 사용자 정보는 src/user/users.csv 파일에 저장
    private static final String FILE_PATH = "src/user/users.csv";

    public UserRepository() {
        initializeStorage();
    }

    // 회원가입 처리
    public String register(String studentId, String name) {
        if (!initializeStorage()) {
            return RESULT_ERROR;
        }

        String normalizedStudentId = normalize(studentId);
        String normalizedName = normalize(name);

        if (normalizedStudentId.isEmpty() || normalizedName.isEmpty()) {
            return RESULT_INVALID;
        }

        // 0000은 관리자 전용 계정
        if (ADMIN_ID.equals(normalizedStudentId)) {
            return RESULT_ADMIN_RESERVED;
        }

        try {
            if (findByStudentId(normalizedStudentId) != null) {
                return RESULT_DUPLICATE;
            }

            appendUser(new Student(normalizedStudentId, normalizedName));
            return RESULT_SUCCESS;
        } catch (IOException e) {
            return RESULT_ERROR;
        }
    }

    // 로그인 처리
    public LoginResult login(String studentId, String name) {
        if (!initializeStorage()) {
            return new LoginResult(null, "사용자 정보를 불러오지 못했습니다.");
        }

        String normalizedStudentId = normalize(studentId);
        String normalizedName = normalize(name);

        if (normalizedStudentId.isEmpty() || normalizedName.isEmpty()) {
            return new LoginResult(null, "학번과 이름을 모두 입력해주세요.");
        }

        try {
            User user = findByStudentId(normalizedStudentId);

            if (user == null) {
                return new LoginResult(null, "존재하지 않는 학번입니다.");
            }

            if (!user.getName().equals(normalizedName)) {
                return new LoginResult(null, "이름이 일치하지 않습니다.");
            }

            return new LoginResult(user, null);
        } catch (IOException e) {
            return new LoginResult(null, "로그인 중 오류가 발생했습니다.");
        }
    }

    // users.csv 파일과 관리자 계정 초기화
    private boolean initializeStorage() {
        File file = new File(FILE_PATH);

        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }

            if (!file.exists()) {
                file.createNewFile();
            }

            if (findByStudentId(ADMIN_ID) == null) {
                appendUser(new Admin(ADMIN_ID, ADMIN_NAME));
            }

            return true;
        } catch (IOException e) {
            return false;
        }
    }

    // 사용자 1명 저장
    private void appendUser(User user) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(user.getStudentId() + "," + user.getName() + "," + user.getRole());
            writer.newLine();
        }
    }

    // 학번으로 사용자 조회
    private User findByStudentId(String studentId) throws IOException {
        File file = new File(FILE_PATH);

        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");

                if (parts.length < 3) {
                    continue;
                }

                if (!parts[0].equals(studentId)) {
                    continue;
                }

                if ("ADMIN".equals(parts[2])) {
                    return new Admin(parts[0], parts[1]);
                }

                return new Student(parts[0], parts[1]);
            }
        }

        return null;
    }

    private String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
