package PMQ.local.SpringBootProject.modules.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import PMQ.local.SpringBootProject.modules.users.entities.User;

// Mỗi entities sẽ có một repository tương ứng để thực hiện các thao tác CRUD (Create, Read, Update, Delete) trên cơ sở dữ liệu. Repository này sẽ kế thừa từ JpaRepository, cung cấp các phương thức truy vấn cơ bản và có thể định nghĩa thêm các phương thức tùy chỉnh nếu cần.

@Repository // Đánh dấu lớp này là một repository trong Spring, cho phép Spring tự động phát
            // hiện và quản lý nó như một bean trong ứng dụng
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> { // Long là kiểu dữ
                                                                                                    // liệu của id trong
                                                                                                    // entity User
    // Các phương thức truy vấn tùy chỉnh có thể được định nghĩa ở đây nếu cần
    Optional<User> findByEmail(String email); // Tìm kiếm người dùng theo email

    Boolean existsByEmail(String email); // Kiểm tra xem email đã tồn tại trong cơ sở dữ liệu hay chưa
}
