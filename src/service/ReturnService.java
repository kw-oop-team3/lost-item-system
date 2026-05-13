package service;

import item.ItemStatus;
import user.User;

public class ReturnService {
    private ItemService itemService;

    public ReturnService(ItemService itemService) {
        this.itemService = itemService;
    }

    public boolean returnItem(User user, int itemId) {
        if (!user.canChangeStatus()) return false;
        return itemService.changeStatus(user, itemId, ItemStatus.RETURNED);
    }
}
