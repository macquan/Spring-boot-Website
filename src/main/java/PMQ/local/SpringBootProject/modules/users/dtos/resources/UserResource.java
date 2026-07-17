package PMQ.local.SpringBootProject.modules.users.dtos.resources;

import com.fasterxml.jackson.annotation.JsonInclude;

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

    // public UserResource(Long id, String email, String name) {
    // this.id = id;
    // this.email = email;
    // this.name = name;
    // }

    // public Long getId() {
    // return id;
    // }

    // public String getEmail() {
    // return email;
    // }

    // public String getName() {
    // return name;
    // }

}
