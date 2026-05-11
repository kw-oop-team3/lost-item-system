package service;

import item.ItemStatus;
import user.User;

public class ReturnService {
    private ItemService itemService;

    public ReturnService(ItemService itemService) {
        this.itemService = itemService;
    }

    public void returnItem(User user, int itemId) {
        if (!user.canChangeStatus()) {
            System.out.println("반환 처리 권한이 없습니다.");
            return;
        }
        itemService.changeStatus(user, itemId, ItemStatus.RETURNED);
    }
}
