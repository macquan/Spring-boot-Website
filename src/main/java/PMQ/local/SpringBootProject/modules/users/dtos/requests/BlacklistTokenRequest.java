package PMQ.local.SpringBootProject.modules.users.dtos.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class BlacklistTokenRequest {
    @NotBlank(message = "Token is required")
    private String token;

}
