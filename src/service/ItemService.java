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
        if (!user.canRegisterItem()) return null;
        LostItem item = new LostItem(itemName, category, foundLocation, storageLocation, foundDate);
        repository.save(item);
        return item;
    }

    public boolean changeStatus(User user, int itemId, ItemStatus newStatus) {
        if (!user.canChangeStatus()) return false;
        Optional<LostItem> opt = repository.findById(itemId);
        if (!opt.isPresent()) return false;
        opt.get().setStatus(newStatus);
        return true;
    }

    public LostItemRepository getRepository() { return repository; }
}
