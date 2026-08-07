package PMQ.local.SpringBootProject.modules.users.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import PMQ.local.SpringBootProject.modules.users.dtos.requests.RefreshTokenRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.LoginResource;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.RefreshTokenResource;
import PMQ.local.SpringBootProject.modules.users.services.impls.BlacklistService;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.UserServiceInterface;
import PMQ.local.SpringBootProject.resources.APIResource;
import PMQ.local.SpringBootProject.resources.MessageResource;
import PMQ.local.SpringBootProject.services.JwtService;
import jakarta.validation.Valid;

@Validated // Dùng để kích hoạt việc kiểm tra các ràng buộc (constraints) trên các tham số
           // của phương thức trong controller.
@RestController
@RequestMapping("/v1/auth")
public class AuthController {

    private final UserServiceInterface userService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private BlacklistService blacklistService;

    @Autowired
    JwtService jwtService;

    public AuthController(UserServiceInterface userService) {
        this.userService = userService;

    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest request) {
        // LoginResource auth = userService.authenticate(request);

        // return ResponseEntity.ok(auth);

        Object result = userService.authenticate(request);

        if (result instanceof LoginResource loginResource) {
            APIResource<LoginResource> response = APIResource.ok(loginResource, "Login successful");
            return ResponseEntity.ok(response);
        }

        if (result instanceof APIResource errorResource) {
            return ResponseEntity.unprocessableEntity().body(errorResource);
        }

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Network error occurred while processing login request");
    }

    @PostMapping("/blacklisted_tokens")
    public ResponseEntity<?> addTokenToBlacklist(@Valid @RequestBody BlacklistTokenRequest request) {
        try {
            Object result = blacklistService.create(request);
            return ResponseEntity.ok(result);

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(new MessageResource("Error occurred while adding token to blacklist"));
        }

    }

    // Viết thêm đăng xuất, token sẽ được thêm vào danh sách đen trong phương thức
    // logout
    @GetMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String bearerToken) { // RequestHeader để lấy token
                                                                                          // từ header Authorization
        try {

            String token = bearerToken.substring(7); // Loại bỏ "Bearer " khỏi chuỗi token

            BlacklistTokenRequest request = new BlacklistTokenRequest();
            request.setToken(token);
            blacklistService.create(request);

            APIResource<Void> response = APIResource.<Void>builder().success(true)
                    .message("Successfully logged out").status(HttpStatus.OK).build();

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            APIResource<Void> errorResponse = APIResource.<Void>builder().success(false)
                    .message("Error occurred while logging out").status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            return ResponseEntity.internalServerError()
                    .body(errorResponse);
        }
    }

    // Thêm phương thức refresh token
    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody RefreshTokenRequest request) {
        String refreshToken = request.getRefreshToken();
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            return ResponseEntity.status(401).body(new MessageResource("Invalid refresh token"));
        }

        Long userId = Long.valueOf(jwtService.getUserIdFromJwt(refreshToken));
        String email = jwtService.getEmailFromJwt(refreshToken);
        String newToken = jwtService.generateToken(userId, email, null);
        String newRefreshToken = jwtService.generateRefreshToken(userId, email);

        return ResponseEntity.ok(new RefreshTokenResource(newToken, newRefreshToken));
    }
}
