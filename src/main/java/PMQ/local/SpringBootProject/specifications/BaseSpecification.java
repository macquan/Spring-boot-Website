package PMQ.local.SpringBootProject.specifications;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;

// Lớp BaseSpecification này sẽ chứa các phương thức tĩnh để tạo ra các Specification cho các thực thể khác nhau trong ứng dụng. Các Specification này sẽ được sử dụng để xây dựng các truy vấn động dựa trên các tham số lọc được cung cấp.

public class BaseSpecification<T> {

    private static final Logger logger = LoggerFactory.getLogger(BaseSpecification.class);

    // String keyword, String... fields: keyword là từ khóa tìm kiếm, fields là các
    // trường dữ liệu mà từ khóa sẽ được tìm kiếm trong đó.
    public static <T> Specification<T> keyword(String keyword, String... fields) {
        // Trả về một Specification dựa trên từ khóa và các trường dữ liệu được cung
        // cấp. Nếu từ khóa là null hoặc rỗng, nó sẽ trả về một điều kiện luôn đúng
        // (conjunction). Nếu không, nó sẽ tạo ra một mảng các Predicate, mỗi Predicate
        // đại diện cho một điều kiện LIKE trên từng trường dữ liệu, và kết hợp chúng
        // bằng toán tử OR.
        return (root, query, criteriaBuilder) -> {
            if (keyword == null || keyword.isEmpty()) {
                return criteriaBuilder.conjunction(); // Nếu từ khóa rỗng hoặc null, trả về một điều kiện luôn đúng
                                                      // (conjunction).
            }

            Predicate[] predicates = new Predicate[fields.length]; // Tạo một mảng Predicate với kích thước bằng số
                                                                   // lượng trường dữ liệu.
            for (int i = 0; i < fields.length; i++) {
                predicates[i] = criteriaBuilder.like(
                        criteriaBuilder.lower(root.get(fields[i])), // Chuyển trường dữ liệu thành chữ thường để tìm
                                                                    // kiếm không phân biệt chữ hoa thường.
                        "%" + keyword.toLowerCase() + "%" // Tạo điều kiện LIKE với từ khóa cũng được chuyển
                                                          // thành chữ thường.
                );
            }

            return criteriaBuilder.or(predicates); // Kết hợp tất cả các điều kiện LIKE bằng toán tử OR.

        };
    }

    public static <T> Specification<T> whereSpec(Map<String, String> filters) {
        return (root, query, criteriaBuilder) -> {
            // Tạo một danh sách các Predicate từ các bộ lọc được cung cấp. Mỗi bộ lọc sẽ
            // tạo ra một điều kiện bằng cách sử dụng phương thức equal của CriteriaBuilder.
            List<Predicate> predicates = filters.entrySet().stream()
                    .map(entry -> criteriaBuilder.equal(root.get(entry.getKey()), entry.getValue()))
                    .collect(Collectors.toList());

            // Kết hợp tất cả các Predicate bằng toán tử AND và trả về kết quả.
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

    public static <T> Specification<T> complexWhereSpec(Map<String, Map<String, String>> filters) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = filters.entrySet().stream()
                    .flatMap(entry -> entry.getValue().entrySet().stream()
                            .map(condition -> {
                                String field = entry.getKey();
                                String operator = condition.getKey();
                                String value = condition.getValue();
                                switch (operator.toLowerCase()) {
                                    case "eq" -> {
                                        return criteriaBuilder.equal(root.get(field), value);
                                    }
                                    case "gt" -> {
                                        return criteriaBuilder.greaterThan(root.get(field), value);
                                    }
                                    case "lt" -> {
                                        return criteriaBuilder.lessThan(root.get(field), value);
                                    }
                                    case "gte" -> {
                                        return criteriaBuilder.greaterThanOrEqualTo(root.get(field), value);
                                    }
                                    case "lte" -> {
                                        return criteriaBuilder.lessThanOrEqualTo(root.get(field), value);
                                    }
                                    case "in" -> {
                                        List<String> values = List.of(value.split(","));
                                        return root.get(field).in(values);
                                    }
                                    default -> throw new IllegalArgumentException("Unsupported operator: " + operator);
                                }
                            }))
                    .collect(Collectors.toList());
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        };
    }

}
