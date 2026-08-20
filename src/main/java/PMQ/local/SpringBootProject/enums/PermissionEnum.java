package PMQ.local.SpringBootProject.enums;

// Enum này định nghĩa các quyền truy cập khác nhau trong hệ thống, mỗi quyền được biểu diễn bằng một chuỗi prefix duy nhất.
public enum PermissionEnum {
    USER_CATALOGUE("user_catalogue"),
    PERMISSION("permission"),
    USER("user");

    private final String prefix;

    PermissionEnum(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }

}
