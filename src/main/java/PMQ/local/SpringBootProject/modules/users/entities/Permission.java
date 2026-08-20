package PMQ.local.SpringBootProject.modules.users.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "permissions")
public class Permission {

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

    @Column(name = "user_id")
    private Long userId;

    @PrePersist // set dữ liệu cho lần đầu tiên
    protected void onCreated() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate // set dữ liệu cho lần update
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    @ManyToMany(mappedBy = "permissions") // mappedBy = "permissions": Điều này chỉ ra rằng mối quan hệ nhiều-nhiều giữa
                                          // Permission và Role được ánh xạ
    @JsonBackReference // quan hệ con
    private Set<UserCatalogue> userCatalogues = new HashSet<>(); // Set<UserCatalogue>: Sử dụng Set để đảm bảo rằng
                                                                 // không có phần tử trùng lặp trong tập hợp các
                                                                 // UserCatalogue liên quan đến Permission. HashSet là
                                                                 // một triển khai phổ biến của Set, cung cấp hiệu suất
                                                                 // tốt cho các thao tác thêm, xóa và kiểm tra sự tồn
                                                                 // tại của phần tử.
}
