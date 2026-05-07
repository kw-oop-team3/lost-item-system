
public enum Location {
    NURI("누리관"),
    LIBRARY("중앙도서관"),
    ENGINEERING("공학관"),
    STUDENT_HALL("학생회관"),
    BITGOEUL("빛고을관"),
    ETC("기타");

    private final String displayName;

    Location(String displayName) { this.displayName = displayName; }

    public String getDisplayName() { return displayName; }
}
