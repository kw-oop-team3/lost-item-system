
import java.time.LocalDate;

public class LostItem {
    private static int idCounter = 1;

    private int id;
    private String itemName;
    private Category category;
    private Location foundLocation;
    private String storageLocation;
    private LocalDate foundDate;
    private ItemStatus status;
    private String imagePath;

    public LostItem(String itemName, Category category,
                    Location foundLocation, String storageLocation,
                    LocalDate foundDate) {
        // TODO: 구현
    }

    public int getId() { return id; }
    public String getItemName() { return itemName; }
    public Category getCategory() { return category; }
    public Location getFoundLocation() { return foundLocation; }
    public String getStorageLocation() { return storageLocation; }
    public LocalDate getFoundDate() { return foundDate; }
    public ItemStatus getStatus() { return status; }
    public String getImagePath() { return imagePath; }

    public void setStatus(ItemStatus status) { this.status = status; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        // TODO: 구현
        return "";
    }
}
