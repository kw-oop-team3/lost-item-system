
import java.time.LocalDate;

public class ItemService {
    private LostItemRepository repository;

    public ItemService(LostItemRepository repository) {
        this.repository = repository;
    }

    public LostItem registerItem(User user, String itemName, Category category,
                                  Location foundLocation, String storageLocation,
                                  LocalDate foundDate) {
        // TODO: 구현
        return null;
    }

    public void changeStatus(User user, int itemId, ItemStatus newStatus) {
        // TODO: 구현
    }

    public LostItemRepository getRepository() { return repository; }
}
