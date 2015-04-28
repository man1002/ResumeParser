package resume.parser.enums;

public enum DaysEnum {
    TODAY("Сегодня"),
    YESTERDAY("Вчера")
    ;

    private final String value;

    private DaysEnum(final String value) {
        this.value = value;
    }

    public String getDay() {
        return value;
    }
}