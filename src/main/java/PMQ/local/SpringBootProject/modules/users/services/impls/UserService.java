package PMQ.local.SpringBootProject.modules.users.services.impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.LoginRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.LoginResource;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.UserResource;
import PMQ.local.SpringBootProject.modules.users.entities.User;
import PMQ.local.SpringBootProject.modules.users.repositories.UserRepository;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.UserServiceInterface;
import PMQ.local.SpringBootProject.resources.APIResource;
import PMQ.local.SpringBootProject.services.JwtService;

@Service
public class UserService implements UserServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Value("${jwt.defaultExpiration}")
    private long defaultExpiration;

    @Override
    public Object authenticate(LoginRequest request) {
        try {
            User user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(
                            () -> new BadCredentialsException("Email or password is incorrect"));

            if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
                throw new BadCredentialsException("Email or password is incorrect");
            }

            UserResource userResource = new UserResource(user.getId(), user.getEmail(), user.getName(),
                    user.getPhone());
            String token = jwtService.generateToken(user.getId(), user.getEmail(), defaultExpiration);

            String refreshToken = jwtService.generateRefreshToken(user.getId(), user.getEmail());

            return new LoginResource(token, refreshToken, userResource);
        } catch (BadCredentialsException e) {
            logger.error("Authentication failed: {}", e.getMessage());

            return APIResource.error("AUTH_ERROR: ", e.getMessage(), HttpStatus.UNAUTHORIZED);
        }

    }
}
