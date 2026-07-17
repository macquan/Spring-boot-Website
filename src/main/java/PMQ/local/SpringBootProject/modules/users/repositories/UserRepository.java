package PMQ.local.SpringBootProject.modules.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PMQ.local.SpringBootProject.modules.users.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> { // Long là kiểu dữ liệu của id trong entity User
    // Các phương thức truy vấn tùy chỉnh có thể được định nghĩa ở đây nếu cần
    Optional<User> findByEmail(String email); // Tìm kiếm người dùng theo email
}
