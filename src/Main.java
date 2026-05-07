

public class Main {
    public static void main(String[] args) {
        LostItemRepository repository = new LostItemRepository();
        ItemService itemService = new ItemService(repository);
        ReturnService returnService = new ReturnService(itemService);

        // TODO: 로그인 및 메뉴 흐름 구현
    }
}
