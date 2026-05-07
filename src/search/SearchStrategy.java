
import java.util.List;

public interface SearchStrategy {
    List<LostItem> search(List<LostItem> items, String keyword);
}
