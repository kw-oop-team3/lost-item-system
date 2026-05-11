package item;

public enum Category {
    ELECTRONICS("전자기기"),
    CARD("카드/학생증"),
    CLOTHING("의류"),
    DAILY("생활용품"),
    ETC("기타");

    private final String displayName;

    Category(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }
}
