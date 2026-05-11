import java.util.Scanner;
import user.LoginResult;
import user.User;
import user.UserRepository;

public class Main {
    public static void main(String[] args) {
        LostItemRepository repository = new LostItemRepository();
        ItemService itemService = new ItemService(repository);
        ReturnService returnService = new ReturnService(itemService);

        Scanner scanner = new Scanner(System.in);
        UserRepository userRepository = new UserRepository();

        // TODO: 로그인 및 메뉴 흐름 구현
        // 로그인 성공 전까지는 null
        User loggedInUser = null;

        // 로그인할 때까지 반복
        while (loggedInUser == null) {
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("0. 종료");
            System.out.print("선택: ");
            String choice = scanner.nextLine().trim();

            // 로그인
            if (choice.equals("1")) {
                System.out.print("학번 입력: ");
                String studentId = scanner.nextLine().trim();

                System.out.print("이름 입력: ");
                String name = scanner.nextLine().trim();

                LoginResult result = userRepository.login(studentId, name);

                if (result.isSuccess()) {
                    loggedInUser = result.getUser();
                } else {
                    System.out.println(result.getMessage());
                    System.out.println();
                }

            // 회원가입
            } else if (choice.equals("2")) {
                System.out.print("학번 입력: ");
                String studentId = scanner.nextLine().trim();

                System.out.print("이름 입력: ");
                String name = scanner.nextLine().trim();

                String result = userRepository.register(studentId, name);

                switch (result) {
                    case UserRepository.RESULT_SUCCESS:
                        System.out.println("회원가입 성공! 로그인해주세요.");
                        break;
                    case UserRepository.RESULT_DUPLICATE:
                        System.out.println("이미 존재하는 학번입니다.");
                        break;
                    case UserRepository.RESULT_INVALID:
                        System.out.println("학번과 이름을 모두 입력해주세요.");
                        break;
                    case UserRepository.RESULT_ADMIN_RESERVED:
                        System.out.println("0000 학번은 관리자 전용 계정입니다.");
                        break;
                    default:
                        System.out.println("저장 중 오류가 발생했습니다.");
                }

                System.out.println();

            // 종료
            } else if (choice.equals("0")) {
                System.out.println("프로그램을 종료합니다.");
                scanner.close();
                return;

            // 잘못된 입력
            } else {
                System.out.println("올바른 메뉴를 선택해주세요.");
                System.out.println();
            }
        }

        // 로그인 성공 후 사용자 정보 출력
        System.out.println("환영합니다, " + loggedInUser.getName() + "님! (" + loggedInUser.getRole() + ")");

        scanner.close();
    }
}
