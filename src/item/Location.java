package item;

public enum Location {
    HWADO("화도관"),
    BIMA("비마관"),
    OKUI("옥의관"),
    LIBRARY("중앙도서관"),
    WELFARE("복지관"),
    RESEARCH("연구관"),
    CHAMBBIT("참빛관"),
    HANUL("한울관"),
    NURI("누리관"),
    SAEBBIT("새빛관"),
    BITSOL("빛솔재"),
    OPEN_THEATER("노천극장"),
    DONGHAE("동해문화예술관"),
    ANNIVERSARY("80주년 기념관"),
    ETC("기타");

    private final String displayName;

    Location(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }
}
