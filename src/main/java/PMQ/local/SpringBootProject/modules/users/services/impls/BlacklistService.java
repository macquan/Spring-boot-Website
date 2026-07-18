package PMQ.local.SpringBootProject.modules.users.services.impls;

import java.time.ZoneId;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.BlacklistTokenRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.MessageResource;
import PMQ.local.SpringBootProject.modules.users.entities.BlacklistedToken;
import PMQ.local.SpringBootProject.modules.users.repositories.BlacklistedTokenRepository;
import PMQ.local.SpringBootProject.services.JwtService;
import io.jsonwebtoken.Claims;

@Service // Dùng để đánh dấu lớp này là một service trong Spring Boot, giúp Spring Boot
         // tự động phát hiện và quản lý bean của lớp này, kiểm tra các điều kiện logic
         // của service trước khi gọi tầng @Repository để tương tác với database
public class BlacklistService {
    // Viết 2 phương thức để thêm token vào bảng blacklisted_tokens và kiểm tra xem
    // token có trong danh sách đen hay không

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    private JwtService jwtService;

    private static final Logger logger = LoggerFactory.getLogger(BlacklistService.class);

    public Object create(BlacklistTokenRequest request) {
        try {
            if (blacklistedTokenRepository.existsByToken(request.getToken())) {
                return new MessageResource("Token already exists in the blacklist");
            }

            Claims claims = jwtService.getAllClaimsFromToken(request.getToken());

            Long userId = Long.valueOf(claims.getSubject());

            Date expiryDate = claims.getExpiration();

            BlacklistedToken blacklistedToken = new BlacklistedToken();
            blacklistedToken.setToken(request.getToken());
            blacklistedToken.setUserId(userId);
            blacklistedToken.setExpiryDate(expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            blacklistedTokenRepository.save(blacklistedToken);

            logger.info("Token added to blacklist successfully: {}", request.getToken());

            return new MessageResource("Token added to blacklist successfully");

        }

        catch (Exception e) {
            return new MessageResource("Network error occurred while adding token to blacklist" + e.getMessage());
        }
    }
}
