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

// Cứ mỗi table sẽ có một entity tương ứng, và mỗi entity sẽ có một repository tương ứng. Repository này sẽ được sử dụng để thực hiện các thao tác CRUD (Create, Read, Update, Delete) trên cơ sở dữ liệu. Trong trường hợp này, RefreshToken là entity cho table refresh_tokens, cho phép bạn lưu trữ và truy xuất các token làm mới.

@NoArgsConstructor
@AllArgsConstructor
@Data // Dùng để tự động tạo các phương thức getter, setter, toString, equals và
      // hashCode
@Entity // Dùng để đánh dấu lớp này là một thực thể (entity) trong JPA
@Table(name = "refresh_tokens")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Tự động sinh giá trị cho trường id
    private Long id;

    @Column(name = "refresh_token", nullable = false, unique = true)
    private String refreshToken;

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
