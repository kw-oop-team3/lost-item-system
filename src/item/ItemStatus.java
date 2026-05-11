package item;

public enum ItemStatus {
    STORED("보관 중"),
    RETURNED("반환 완료"),
    DISPOSAL("폐기 예정");

    private final String displayName;

    ItemStatus(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }
}
