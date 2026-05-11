package search;

import item.LostItem;
import java.util.ArrayList;
import java.util.List;

public class CategorySearchStrategy implements SearchStrategy {

    @Override
    public List<LostItem> search(List<LostItem> items, String keyword) {
        List<LostItem> result = new ArrayList<>();

        if (items == null || keyword == null) {
            return result;
        }

        String lowerKeyword = keyword.toLowerCase();

        for (LostItem item : items) {
            if (item.getCategory() != null && item.getCategory().getDisplayName().toLowerCase().contains(lowerKeyword)) {
                result.add(item);
            }
        }
        return result;
    }
}
