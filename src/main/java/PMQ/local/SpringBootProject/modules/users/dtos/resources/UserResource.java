package PMQ.local.SpringBootProject.modules.users.dtos.resources;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;

import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@Builder
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL) // Tránh việc hiển thị các trường null trong JSON response
public class UserResource {
    private final Long id;
    private final String email;
    private final String name;
    private final String phone;
    private final Set<UserCatalogue> userCatalogues;

}
