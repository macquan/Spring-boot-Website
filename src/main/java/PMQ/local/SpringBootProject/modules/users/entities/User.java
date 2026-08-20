package PMQ.local.SpringBootProject.modules.users.entities;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
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

// Cứ mỗi table sẽ có một entity tương ứng, và mỗi entity sẽ có một repository tương ứng. Repository này sẽ được sử dụng để thực hiện các thao tác CRUD (Create, Read, Update, Delete) trên cơ sở dữ liệu. Trong trường hợp này, User là entity cho table users, cho phép bạn lưu trữ và truy xuất thông tin người dùng.

@Builder // Tạo một builder cho lớp User, cho phép tạo các đối tượng User một cách linh
         // hoạt và dễ đọc. toBuilder = true cho phép tạo một builder từ một đối tượng
         // hiện có.
@NoArgsConstructor // Tự động tạo constructor không tham số
@AllArgsConstructor // Tự động tạo constructor không tham số và constructor với tất cả các tham số
@Data // Tự động tạo các phương thức getter, setter, toString, equals và hashCode
@Entity // Đánh dấu lớp này là một thực thể JPA, ánh xạ với bảng trong cơ sở dữ liệu
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Builder.Default
    @ManyToMany(mappedBy = "users")
    @JsonBackReference
    private Set<UserCatalogue> userCatalogues = new HashSet<>();

    private String name;
    private String email;
    private String password;
    private String phone;
    private String image;
    private String address;

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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User that = (User) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
