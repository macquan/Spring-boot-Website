package PMQ.local.SpringBootProject.modules.users.controllers;

import java.util.Set;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PMQ.local.SpringBootProject.modules.users.dtos.resources.UserCatalogueResource;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.UserResource;
import PMQ.local.SpringBootProject.modules.users.entities.User;
import PMQ.local.SpringBootProject.modules.users.repositories.UserRepository;
import PMQ.local.SpringBootProject.resources.APIResource;

@RestController
@RequestMapping("/api/v1")
public class UserController {
        // Đây là một route để lấy thông tin người dùng hiện tại. Trong thực tế, bạn sẽ
        // cần xác thực người dùng và lấy thông tin từ token hoặc session thay vì sử
        // dụng email cứng.
        @Autowired
        private UserRepository userRepository;

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
