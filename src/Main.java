import item.Category;
import item.ItemStatus;
import item.Location;
import item.LostItem;
import repository.LostItemRepository;
import search.CategorySearchStrategy;
import search.LocationSearchStrategy;
import search.NameSearchStrategy;
import search.SearchStrategy;
import service.ItemService;
import service.ReturnService;
import user.LoginResult;
import user.User;
import user.UserRepository;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        LostItemRepository repository = new LostItemRepository();
        ItemService itemService = new ItemService(repository);
        ReturnService returnService = new ReturnService(itemService);
        UserRepository userRepository = new UserRepository();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            User loggedInUser = loginMenu(scanner, userRepository);
            if (loggedInUser == null) {
                System.out.println("프로그램을 종료합니다.");
                break;
            }
            System.out.println("\n환영합니다, " + loggedInUser.getName() + "님! (" + loggedInUser.getRole() + ")");
            mainMenu(scanner, loggedInUser, itemService, returnService, repository);
        }

        scanner.close();
    }

    private static User loginMenu(Scanner scanner, UserRepository userRepository) {
        while (true) {
            System.out.println("\n=== KW Lost and Found ===");
            System.out.println("1. 로그인");
            System.out.println("2. 회원가입");
            System.out.println("0. 종료");
            System.out.print("선택: ");
            String choice = scanner.nextLine().trim();

            if (choice.equals("1")) {
                System.out.print("학번 입력: ");
                String studentId = scanner.nextLine().trim();
                System.out.print("이름 입력: ");
                String name = scanner.nextLine().trim();

                LoginResult result = userRepository.login(studentId, name);
                if (result.isSuccess()) {
                    return result.getUser();
                }
                System.out.println(result.getMessage());

            } else if (choice.equals("2")) {
                System.out.print("학번 입력: ");
                String studentId = scanner.nextLine().trim();
                System.out.print("이름 입력: ");
                String name = scanner.nextLine().trim();

                String result = userRepository.register(studentId, name);
                switch (result) {
                    case UserRepository.RESULT_SUCCESS:        System.out.println("회원가입 성공! 로그인해주세요."); break;
                    case UserRepository.RESULT_DUPLICATE:      System.out.println("이미 존재하는 학번입니다."); break;
                    case UserRepository.RESULT_INVALID:        System.out.println("학번과 이름을 모두 입력해주세요."); break;
                    case UserRepository.RESULT_ADMIN_RESERVED: System.out.println("0000 학번은 관리자 전용 계정입니다."); break;
                    default:                                    System.out.println("저장 중 오류가 발생했습니다."); break;
                }

            } else if (choice.equals("0")) {
                return null;

            } else {
                System.out.println("올바른 메뉴를 선택해주세요.");
            }
        }
    }

    private static void mainMenu(Scanner scanner, User user,
                                  ItemService itemService, ReturnService returnService,
                                  LostItemRepository repository) {
        while (true) {
            System.out.println();
            if (user.canRegisterItem()) {
                System.out.println("=== 관리자 메뉴 ===");
                System.out.println("1. 분실물 등록");
                System.out.println("2. 분실물 전체 조회");
                System.out.println("3. 분실물 검색");
                System.out.println("4. 분실물 상태 변경");
                System.out.println("5. 반환 처리");
                System.out.println("0. 로그아웃");
            } else {
                System.out.println("=== 학생 메뉴 ===");
                System.out.println("1. 분실물 전체 조회");
                System.out.println("2. 분실물 검색");
                System.out.println("0. 로그아웃");
            }
            System.out.print("선택: ");
            String choice = scanner.nextLine().trim();

            if (user.canRegisterItem()) {
                switch (choice) {
                    case "1": handleRegisterItem(scanner, itemService, user); break;
                    case "2": handleListAll(scanner, repository); break;
                    case "3": handleSearch(scanner, repository); break;
                    case "4": handleChangeStatus(scanner, itemService, user); break;
                    case "5": handleReturn(scanner, returnService, user); break;
                    case "0": System.out.println("로그아웃합니다."); return;
                    default:  System.out.println("올바른 메뉴를 선택해주세요.");
                }
            } else {
                switch (choice) {
                    case "1": handleListAll(scanner, repository); break;
                    case "2": handleSearch(scanner, repository); break;
                    case "0": System.out.println("로그아웃합니다."); return;
                    default:  System.out.println("올바른 메뉴를 선택해주세요.");
                }
            }
        }
    }

    private static void handleRegisterItem(Scanner scanner, ItemService itemService, User user) {
        System.out.print("물건 이름: ");
        String itemName = scanner.nextLine().trim();
        if (itemName.isEmpty()) {
            System.out.println("물건 이름을 입력해주세요.");
            return;
        }

        Category category = selectCategory(scanner);
        if (category == null) return;

        Location foundLocation = selectLocation(scanner);
        if (foundLocation == null) return;

        System.out.print("보관 장소: ");
        String storageLocation = scanner.nextLine().trim();

        LocalDate foundDate = inputDate(scanner);
        if (foundDate == null) return;

        LostItem item = itemService.registerItem(user, itemName, category, foundLocation, storageLocation, foundDate);
        if (item == null) return;

        System.out.println("등록 완료: " + item);

        // 사진 등록 여부 확인
        System.out.print("사진을 등록하시겠습니까? (y/n): ");
        if (scanner.nextLine().trim().equalsIgnoreCase("y")) {
            String imagePath = chooseImageFile();
            if (imagePath != null) {
                item.setImagePath(imagePath);
                System.out.println("사진이 등록되었습니다.");
            } else {
                System.out.println("사진 선택이 취소되었습니다.");
            }
        }
    }

    private static void handleListAll(Scanner scanner, LostItemRepository repository) {
        List<LostItem> items = repository.findAll();
        if (items.isEmpty()) {
            System.out.println("등록된 분실물이 없습니다.");
            return;
        }
        System.out.println("=== 분실물 목록 (" + items.size() + "건) ===");
        for (LostItem item : items) {
            String photoMark = item.getImagePath() != null ? " [사진 있음]" : "";
            System.out.println(item + photoMark);
        }

        promptViewPhoto(scanner, repository);
    }

    private static void handleSearch(Scanner scanner, LostItemRepository repository) {
        System.out.println("검색 기준  1. 물건 이름  2. 발견 장소  3. 카테고리");
        System.out.print("선택: ");
        String type = scanner.nextLine().trim();

        SearchStrategy strategy;
        switch (type) {
            case "1": strategy = new NameSearchStrategy(); break;
            case "2": strategy = new LocationSearchStrategy(); break;
            case "3": strategy = new CategorySearchStrategy(); break;
            default:
                System.out.println("올바른 검색 기준을 선택해주세요.");
                return;
        }

        System.out.print("검색어: ");
        String keyword = scanner.nextLine().trim();

        List<LostItem> results = repository.search(strategy, keyword);
        if (results.isEmpty()) {
            System.out.println("검색 결과가 없습니다.");
            return;
        }

        System.out.println("=== 검색 결과 " + results.size() + "건 ===");
        for (LostItem item : results) {
            String photoMark = item.getImagePath() != null ? " [사진 있음]" : "";
            System.out.println(item + photoMark);
        }

        promptViewPhoto(scanner, repository);
    }

    // 목록 출력 후 사진 조회 제안
    private static void promptViewPhoto(Scanner scanner, LostItemRepository repository) {
        System.out.print("\n사진을 볼 분실물 ID (엔터 시 건너뜀): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) return;

        try {
            int id = Integer.parseInt(input);
            repository.findById(id).ifPresent(item -> showImage(item));
        } catch (NumberFormatException e) {
            System.out.println("올바른 ID를 입력해주세요.");
        }
    }

    private static void handleChangeStatus(Scanner scanner, ItemService itemService, User user) {
        System.out.print("분실물 ID: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("올바른 ID를 입력해주세요.");
            return;
        }

        System.out.println("변경할 상태  1. 보관 중  2. 반환 완료  3. 폐기 예정");
        System.out.print("선택: ");
        String statusChoice = scanner.nextLine().trim();

        ItemStatus newStatus;
        switch (statusChoice) {
            case "1": newStatus = ItemStatus.STORED; break;
            case "2": newStatus = ItemStatus.RETURNED; break;
            case "3": newStatus = ItemStatus.DISPOSAL; break;
            default:
                System.out.println("올바른 상태를 선택해주세요.");
                return;
        }

        itemService.changeStatus(user, id, newStatus);
    }

    private static void handleReturn(Scanner scanner, ReturnService returnService, User user) {
        System.out.print("반환할 분실물 ID: ");
        int id;
        try {
            id = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("올바른 ID를 입력해주세요.");
            return;
        }
        returnService.returnItem(user, id);
    }

    // ── Swing: 이미지 파일 선택 ──────────────────────────────────────
    private static String chooseImageFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("사진 파일 선택");
        chooser.setFileFilter(new FileNameExtensionFilter(
                "이미지 파일 (JPG, PNG, GIF)", "jpg", "jpeg", "png", "gif"));

        int result = chooser.showOpenDialog(null);
        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }

    // ── Swing: 이미지 창 표시 ────────────────────────────────────────
    private static void showImage(LostItem item) {
        if (item.getImagePath() == null) {
            System.out.println("등록된 사진이 없습니다.");
            return;
        }

        File file = new File(item.getImagePath());
        if (!file.exists()) {
            System.out.println("이미지 파일을 찾을 수 없습니다: " + item.getImagePath());
            return;
        }

        SwingUtilities.invokeLater(() -> {
            ImageIcon original = new ImageIcon(item.getImagePath());
            Image scaled = original.getImage().getScaledInstance(480, -1, Image.SCALE_SMOOTH);

            JLabel imageLabel = new JLabel(new ImageIcon(scaled));
            JLabel infoLabel  = new JLabel(item.toString(), SwingConstants.CENTER);
            infoLabel.setBorder(BorderFactory.createEmptyBorder(6, 8, 8, 8));

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(imageLabel, BorderLayout.CENTER);
            panel.add(infoLabel,  BorderLayout.SOUTH);

            JFrame frame = new JFrame("[" + item.getId() + "] " + item.getItemName());
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.getContentPane().add(panel);
            frame.pack();
            frame.setLocationRelativeTo(null);
            frame.setVisible(true);
        });
    }

    // ── 입력 헬퍼 ────────────────────────────────────────────────────
    private static Category selectCategory(Scanner scanner) {
        System.out.println("카테고리");
        Category[] categories = Category.values();
        for (int i = 0; i < categories.length; i++) {
            System.out.println("  " + (i + 1) + ". " + categories[i].getDisplayName());
        }
        System.out.print("선택: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= categories.length) {
                System.out.println("올바른 카테고리를 선택해주세요.");
                return null;
            }
            return categories[idx];
        } catch (NumberFormatException e) {
            System.out.println("올바른 번호를 입력해주세요.");
            return null;
        }
    }

    private static Location selectLocation(Scanner scanner) {
        System.out.println("발견 장소");
        Location[] locations = Location.values();
        for (int i = 0; i < locations.length; i++) {
            System.out.println("  " + (i + 1) + ". " + locations[i].getDisplayName());
        }
        System.out.print("선택: ");
        try {
            int idx = Integer.parseInt(scanner.nextLine().trim()) - 1;
            if (idx < 0 || idx >= locations.length) {
                System.out.println("올바른 장소를 선택해주세요.");
                return null;
            }
            return locations[idx];
        } catch (NumberFormatException e) {
            System.out.println("올바른 번호를 입력해주세요.");
            return null;
        }
    }

    private static LocalDate inputDate(Scanner scanner) {
        System.out.print("발견 날짜 (YYYY-MM-DD, 엔터 시 오늘 날짜): ");
        String input = scanner.nextLine().trim();
        if (input.isEmpty()) {
            return LocalDate.now();
        }
        try {
            return LocalDate.parse(input);
        } catch (DateTimeParseException e) {
            System.out.println("날짜 형식이 올바르지 않습니다. (예: 2025-05-01)");
            return null;
        }
    }
}
