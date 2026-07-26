package PMQ.local.SpringBootProject.modules.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PMQ.local.SpringBootProject.modules.users.entities.RefreshToken;

// Cứ mỗi table sẽ có một entity tương ứng, và mỗi entity sẽ có một repository tương ứng. Repository này sẽ được sử dụng để thực hiện các thao tác CRUD (Create, Read, Update, Delete) trên cơ sở dữ liệu. Trong trường hợp này, RefreshTokenRepository là repository cho entity RefreshToken, cho phép bạn lưu trữ và truy xuất các token làm mới.

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    // Các phương thức truy vấn tùy chỉnh có thể được định nghĩa ở đây nếu cần
    boolean existsByRefreshToken(String refreshToken); // Kiểm tra xem token làm mới có tồn tại trong cơ sở dữ liệu hay
                                                       // không

    Optional<RefreshToken> findByRefreshToken(String refreshToken); // Tìm kiếm token làm mới theo giá trị của nó
}
