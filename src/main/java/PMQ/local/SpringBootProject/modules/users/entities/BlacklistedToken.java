package PMQ.local.SpringBootProject.modules.users.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data // Dùng để tự động tạo các phương thức getter, setter, toString, equals và
      // hashCode
@Entity // Dùng để đánh dấu lớp này là một thực thể (entity) trong JPA
@Table(name = "blacklisted_tokens")
public class BlacklistedToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị cho trường id
    private Long id;

    @Column(nullable = false, unique = true)
    private String token;

    @Column(name = "expiry_date", nullable = false)
    private LocalDateTime expiryDate;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @CreationTimestamp // Đánh dấu trường này sẽ được tự động gán giá trị thời gian hiện tại khi bản
                       // ghi được tạo
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp // Đánh dấu trường này sẽ được tự động gán giá trị thời gian hiện tại khi bản
                     // ghi được cập nhật
    @Column(name = "updated_at") // Đặt tên cột trong cơ sở dữ liệu
    private LocalDateTime updatedAt;

}
