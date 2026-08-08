package PMQ.local.SpringBootProject.services;

import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class BaseService {
    protected Sort createSort(String sortParam) {
        if (sortParam == null || sortParam.isEmpty()) {
            return Sort.by(Sort.Order.desc("id"));
        }

        String[] parts = sortParam.split(","); // Dùng để tách chuỗi sortParam thành các phần tử dựa trên dấu phẩy.
        String field = parts[0]; // Lấy tên trường để sắp xếp từ phần tử đầu tiên.
        String sortDirection = (parts.length > 1) ? parts[1] : "asc"; // Lấy hướng sắp xếp từ phần tử thứ hai, mặc định
                                                                      // là "asc" nếu không có.

        if ("desc".equalsIgnoreCase(sortDirection)) { // Kiểm tra xem hướng sắp xếp có phải là "desc" hay không (không
                                                      // phân biệt chữ hoa chữ thường).
            return Sort.by(Sort.Order.desc(field));
        } else {
            return Sort.by(Sort.Order.asc(field));
        }
    }
}
