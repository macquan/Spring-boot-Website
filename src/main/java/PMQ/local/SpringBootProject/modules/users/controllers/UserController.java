package PMQ.local.SpringBootProject.modules.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PMQ.local.SpringBootProject.controllers.BaseController;
import PMQ.local.SpringBootProject.enums.PermissionEnum;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.User.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.User.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.UserResource;
import PMQ.local.SpringBootProject.modules.users.entities.User;
import PMQ.local.SpringBootProject.modules.users.mappers.UserMapper;
import PMQ.local.SpringBootProject.modules.users.repositories.UserRepository;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.UserServiceInterface;
import PMQ.local.SpringBootProject.resources.APIResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.transaction.Transactional;

@Tag(name = "User API", description = "API for managing users")
@RestController
@RequestMapping("/api/v1/users")
public class UserController
                extends BaseController<User, UserResource, StoreRequest, UpdateRequest, UserRepository> {
        // Đây là một route để lấy thông tin người dùng hiện tại. Trong thực tế, bạn sẽ
        // cần xác thực người dùng và lấy thông tin từ token hoặc session thay vì sử
        // dụng email cứng.
        @Autowired
        private UserRepository userRepository;

        public UserController(UserServiceInterface service,
                        UserMapper mapper, UserRepository repo) {

                super(service, mapper, repo, PermissionEnum.USER);
        }

        @Operation(summary = "Get current user", description = "Retrieve information about the currently authenticated user.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "User information retrieved successfully", content = @Content(schema = @Schema(implementation = APIResource.class))),
                        @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema(implementation = APIResource.class)))
        })
        @Transactional()
        @GetMapping("/me")
        public ResponseEntity<?> me() {
                // String email = "john.doe@example.com";
                String email = SecurityContextHolder.getContext().getAuthentication().getName();

                User user = userRepository.findByEmail(email)
                                .orElseThrow(() -> new RuntimeException("User not found"));

                UserResource userResource = UserResource.builder()
                                .id(user.getId())
                                .email(user.getEmail())
                                .name(user.getName())
                                .phone(user.getPhone())
                                .userCatalogues(user.getUserCatalogues())
                                .build();

                APIResource<UserResource> response = APIResource.ok(userResource,
                                "Successfully retrieved user information");

                return ResponseEntity.ok(response);
        }

}
