
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LostItemRepository {
    private List<LostItem> items = new ArrayList<>();
    
    public void save(LostItem item) {
        if (item != null) {
            items.add(item);
        }
    }
           
    public List<LostItem> findAll() {
        return new ArrayList<>(items);
    }
    
    public Optional<LostItem> findById(int id) {
        for (LostItem item : items) {
            if(item.getId() == id) {
                return Optional.of(item);
            }
        }
        return Optional.empty();
    }

    public List<LostItem> search(SearchStrategy strategy, String keyword) {
        if (strategy == null) {
            return new ArrayList<>();
        }
        return strategy.search(items, keyword);
    }
}
