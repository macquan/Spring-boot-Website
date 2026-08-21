package PMQ.local.SpringBootProject.modules.users.dtos.requests.User;

import java.util.List;

import PMQ.local.SpringBootProject.annotations.UniqueEmail;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class StoreRequest {

    @NotBlank(message = "User name is required")
    private String name;

    @UniqueEmail
    @NotBlank(message = "Email is required")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$", message = "Invalid email format")
    private String email;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Password is required")
    private String password;

    private String address;
    private String image;

    @NotNull(message = "Publish status is required")
    @Min(value = 0, message = "Publish status must be greater than or equal to 0")
    @Max(value = 2, message = "Publish status must be less than or equal to 2") // 0: Unpublished, 1: Published, 2:
                                                                                // Archived
    private Integer publish;

    @NotNull(message = "User catalogues for user are required")
    private List<Long> userCatalogues;
}
