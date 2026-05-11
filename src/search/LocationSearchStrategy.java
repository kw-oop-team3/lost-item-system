package search;

import item.LostItem;
import java.util.ArrayList;
import java.util.List;

public class LocationSearchStrategy implements SearchStrategy {

    @Override
    public List<LostItem> search(List<LostItem> items, String keyword) {
        List<LostItem> result = new ArrayList<>();

        if (items == null || keyword == null) {
            return result;
        }

        String lowerKeyword = keyword.toLowerCase();

        for (LostItem item : items) {
            String foundLocation = "";

            if (item.getFoundLocation() != null) {
                foundLocation = item.getFoundLocation().getDisplayName();
            }

            if (foundLocation.toLowerCase().contains(lowerKeyword)) {
                result.add(item);
            }
        }
        return result;
    }
}
