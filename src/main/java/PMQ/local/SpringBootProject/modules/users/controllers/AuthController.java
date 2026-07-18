package PMQ.local.SpringBootProject.modules.users.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.BlacklistTokenRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.LoginRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.LoginResource;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.MessageResource;
import PMQ.local.SpringBootProject.modules.users.services.impls.BlacklistService;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.UserServiceInterface;
import PMQ.local.SpringBootProject.services.JwtService;
import jakarta.validation.Valid;

@Validated
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserServiceInterface userService;

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    JwtService jwtService;

    public AuthController(UserServiceInterface userService) {
        this.userService = userService;

    }

    @PostMapping("/login")
    public ResponseEntity<LoginResource> login(@Valid @RequestBody LoginRequest request) {
        LoginResource auth = userService.authenticate(request);

        return ResponseEntity.ok(auth);
    }

    @PostMapping("/blacklisted_tokens")
    public ResponseEntity<?> addTokenToBlacklist(@Valid @RequestBody BlacklistTokenRequest request) {
        try {
            Object result = blacklistService.create(request);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new MessageResource("Network error occurred while adding token to blacklist"));
        }

    }

    // Viết thêm đăng xuất, token sẽ được thêm vào danh sách đen trong phương thức
    // logout
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken) {
        try {

            String token = bearerToken.substring(7); // Loại bỏ "Bearer " khỏi chuỗi token

            BlacklistTokenRequest request = new BlacklistTokenRequest();
            request.setToken(token);

            Object message = blacklistService.create(request);

            return ResponseEntity.ok(message);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new MessageResource("Network error occurred while logging out"));
        }
    }
}
