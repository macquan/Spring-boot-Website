package PMQ.local.SpringBootProject.modules.users.entities;

import java.time.LocalDateTime;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Builder(toBuilder = true) // Annotation này cho phép sử dụng Builder Pattern để tạo đối tượng
                           // UserCatalogue một cách linh hoạt và dễ đọc hơn.
@Table(name = "user_catalogues")
public class UserCatalogue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Dùng để tự động tăng giá trị của trường id
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "user_catalogue_permission", joinColumns = @JoinColumn(name = "user_catalogue_id"), inverseJoinColumns = @JoinColumn(name = "permission_id"))
    private Set<Permission> permissions;

    @ManyToMany
    @JoinTable(name = "user_catalogue_user", joinColumns = @JoinColumn(name = "user_catalogue_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

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
