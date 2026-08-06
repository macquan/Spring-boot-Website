package PMQ.local.SpringBootProject.modules.users.repositories;

import java.time.LocalDateTime;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PMQ.local.SpringBootProject.modules.users.entities.BlacklistedToken;

// Cứ mỗi table sẽ có một entity tương ứng, và mỗi entity sẽ có một repository tương ứng. Repository này sẽ được sử dụng để thực hiện các thao tác CRUD (Create, Read, Update, Delete) trên cơ sở dữ liệu. Trong trường hợp này, BlacklistedTokenRepository là repository cho entity BlacklistedToken, cho phép bạn lưu trữ và truy xuất các token bị đưa vào danh sách đen.
// Mỗi entities sẽ có một repository tương ứng để thực hiện các thao tác CRUD (Create, Read, Update, Delete) trên cơ sở dữ liệu. Repository này sẽ kế thừa từ JpaRepository, cung cấp các phương thức truy vấn cơ bản và có thể định nghĩa thêm các phương thức tùy chỉnh nếu cần.

@Repository
public interface BlacklistedTokenRepository extends JpaRepository<BlacklistedToken, Long> {
    // Các phương thức truy vấn tùy chỉnh có thể được định nghĩa ở đây nếu cần
    boolean existsByToken(String token); // Kiểm tra xem token có tồn tại trong danh sách đen hay không

    int deleteByExpiryDateBefore(LocalDateTime currentDateTime); // Xóa các token đã hết hạn khỏi danh sách đen
}
