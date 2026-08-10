package PMQ.local.SpringBootProject.helpers;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class FilterParameters {

    // Phân loại theo keyword
    public static String filterKeyword(Map<String, String[]> parameters) {
        return parameters.containsKey("keyword") ? parameters.get("keyword")[0] : null;
    }

    // Phân loại theo các filter đơn giản (where clause) - các filter không chứa các
    // từ khóa đặc biệt như "[", "keyword", "page", "per_page", "sort"
    public static Map<String, String> filterSimple(Map<String, String[]> parameters) {
        // Lọc các tham số không chứa các từ khóa đặc biệt như "[", "keyword", "page",
        // "per_page", "sort" sau đó chuyển đổi chúng thành một Map<String, String> mới.
        return parameters.entrySet().stream()
                .filter(entry -> !entry.getKey().contains("[") && !entry.getKey().contains("keyword")
                        && !entry.getKey().contains("page") && !entry.getKey().contains("per_page")
                        && !entry.getKey().contains("sort"))
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue()[0])); // Lấy giá trị đầu tiên của
                                                                                             // mảng String[] cho mỗi
                                                                                             // key
    }

    // Phân loại theo các filter phức tạp (có thể là nhiều điều kiện, nhiều trường,
    // nhiều kiểu dữ liệu)
    public static Map<String, Map<String, String>> filterComplex(Map<String, String[]> parameters) {
        // Lọc các tham số chứa dấu "[" để xác định các filter phức tạp, sau đó
        // chuyển đổi chúng thành một Map<String, Map<String, String>> mới.
        return parameters.entrySet().stream()
                .filter(entry -> entry.getKey().contains("["))
                .collect(Collectors.groupingBy(
                        entry -> entry.getKey().split("\\[")[0],
                        Collectors.toMap(
                                entry -> entry.getKey().split("\\[")[1].replace("]", ""),
                                entry -> entry.getValue()[0])));
    }

    // Phân loại theo filter kiểu ngày tháng
    public static Map<String, String> filterDateRange(Map<String, String[]> parameters) {
        Map<String, String> dateRangeFilters = new HashMap<>();

        if (parameters.containsKey("start_date")) {
            dateRangeFilters.put("start_date", parameters.get("start_date")[0]);
        }

        if (parameters.containsKey("end_date")) {
            dateRangeFilters.put("end_date", parameters.get("end_date")[0]);
        }

        return dateRangeFilters;
    }
}
