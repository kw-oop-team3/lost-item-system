package item;

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
        this.id = idCounter++;
        this.itemName = itemName;
        this.category = category;
        this.foundLocation = foundLocation;
        this.storageLocation = storageLocation;
        this.foundDate = foundDate;
        this.status = ItemStatus.STORED;
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
        return String.format("[%d] %s | %s | 발견: %s | 보관: %s | %s | %s",
                id,
                itemName,
                category != null ? category.getDisplayName() : "-",
                foundLocation != null ? foundLocation.getDisplayName() : "-",
                storageLocation != null ? storageLocation : "-",
                foundDate != null ? foundDate.toString() : "-",
                status != null ? status.getDisplayName() : "-");
    }
}
