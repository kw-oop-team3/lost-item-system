package service;

import item.Category;
import item.ItemStatus;
import item.Location;
import item.LostItem;
import repository.LostItemRepository;
import user.User;
import java.time.LocalDate;
import java.util.Optional;

public class ItemService {
    private LostItemRepository repository;

    public ItemService(LostItemRepository repository) {
        this.repository = repository;
    }

    public LostItem registerItem(User user, String itemName, Category category,
                                  Location foundLocation, String storageLocation,
                                  LocalDate foundDate) {
        if (!user.canRegisterItem()) {
            System.out.println("분실물 등록 권한이 없습니다.");
            return null;
        }
        LostItem item = new LostItem(itemName, category, foundLocation, storageLocation, foundDate);
        repository.save(item);
        return item;
    }

    public void changeStatus(User user, int itemId, ItemStatus newStatus) {
        if (!user.canChangeStatus()) {
            System.out.println("상태 변경 권한이 없습니다.");
            return;
        }
        Optional<LostItem> opt = repository.findById(itemId);
        if (!opt.isPresent()) {
            System.out.println("해당 ID의 분실물을 찾을 수 없습니다: " + itemId);
            return;
        }
        opt.get().setStatus(newStatus);
        System.out.println("상태 변경 완료: " + opt.get().getItemName() + " → " + newStatus.getDisplayName());
    }

    public LostItemRepository getRepository() { return repository; }
}
