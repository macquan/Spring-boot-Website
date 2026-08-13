package PMQ.local.SpringBootProject.modules.users.dtos.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Dùng để loại bỏ các trường null khi serializing đối tượng thành JSON
public class PermissionResource {
    private final Long id;
    private final String name;
    private final Integer publish;
}
