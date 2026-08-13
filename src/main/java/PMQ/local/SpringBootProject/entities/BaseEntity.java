package PMQ.local.SpringBootProject.entities;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Data;
import lombok.experimental.SuperBuilder;

@Data
@MappedSuperclass // Annotation này chỉ định rằng lớp này là một lớp cha (superclass) mà các
                  // entity khác có thể kế thừa từ nó. Nó không được ánh xạ trực tiếp tới một bảng
                  // trong cơ sở dữ liệu, nhưng các trường dữ liệu trong lớp này sẽ được kế thừa
                  // bởi các entity con.
@SuperBuilder // Annotation này cho phép sử dụng Builder Pattern để tạo đối tượng BaseEntity
              // một cách linh hoạt và dễ đọc hơn.
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Dùng để tự động tăng giá trị của trường id
    private Long id;

    private String name;

    @Column(name = "publish", nullable = false, columnDefinition = "TINYINT(1)") // // 0: Unpublished, 1: Published, 2:
                                                                                 // Archived
    private Integer publish;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist // set dữ liệu cho lần đầu tiên
    protected void onCreated() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate // set dữ liệu cho lần update
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
