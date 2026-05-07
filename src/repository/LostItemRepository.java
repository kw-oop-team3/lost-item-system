
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LostItemRepository {
    private List<LostItem> items = new ArrayList<>();

    public void save(LostItem item) {
        // TODO: 구현
    }

    public List<LostItem> findAll() {
        // TODO: 구현
        return null;
    }

    public Optional<LostItem> findById(int id) {
        // TODO: 구현
        return Optional.empty();
    }

    public List<LostItem> search(SearchStrategy strategy, String keyword) {
        // TODO: 구현
        return null;
    }
}
