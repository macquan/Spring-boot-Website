package PMQ.local.SpringBootProject.services;

import java.security.Key;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;
import java.util.Optional;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PMQ.local.SpringBootProject.config.JwtConfig;
import PMQ.local.SpringBootProject.modules.users.entities.RefreshToken;
import PMQ.local.SpringBootProject.modules.users.repositories.BlacklistedTokenRepository;
import PMQ.local.SpringBootProject.modules.users.repositories.RefreshTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    private final Key key;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    public JwtService(JwtConfig jwtConfig) {
        this.jwtConfig = jwtConfig;
        this.key = Keys.hmacShaKeyFor(Base64.getEncoder().encode(jwtConfig.getSecretKey().getBytes()));
    }

    public String generateToken(Long userId, String email) {
        logger.info("Generating token ...");
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getExpirationTime());

        return Jwts.builder() // Trả về một chuỗi JWT được tạo ra từ thông tin người dùng và thời gian hết
                              // hạn.
                .setSubject(String.valueOf(userId)) // Đặt subject của token là userId, được chuyển đổi thành chuỗi.
                .claim("email", email)
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String generateRefreshToken(Long userId, String email) {
        logger.info("Generating refresh token ...");
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtConfig.getRefreshTokenExpirationTime());

        String refreshToken = Jwts.builder()
                .setSubject(String.valueOf(userId))
                .claim("email", email)
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();

        // String refreshToken = UUID.randomUUID().toString(); // Sử dụng UUID để tạo
        // refresh token ngẫu nhiên

        LocalDateTime localExpiryDate = expiryDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        Optional<RefreshToken> optionalRefreshToken = refreshTokenRepository.findByUserId(userId);

        if (optionalRefreshToken.isPresent()) { // isPresent() kiểm tra xem Optional có chứa giá trị hay không. Nếu có,
                                                // nó trả về true, ngược lại trả về false.
            RefreshToken dBRefreshToken = optionalRefreshToken.get();
            dBRefreshToken.setRefreshToken(refreshToken);
            dBRefreshToken.setExpiryDate(localExpiryDate);
            refreshTokenRepository.save(dBRefreshToken);

        } else {
            RefreshToken insertToken = new RefreshToken();
            insertToken.setRefreshToken(refreshToken);
            insertToken.setExpiryDate(localExpiryDate);
            insertToken.setUserId(userId);
            refreshTokenRepository.save(insertToken);

        }
        return refreshToken;

    }

    // public String extractUsername(String token) {
    // return extractClaim(token, Claims::getSubject);
    // }

    // private <T> T extractClaim(String token, java.util.function.Function<Claims,
    // T> claimsResolver) {
    // final Claims claims = extractAllClaims(token);
    // return claimsResolver.apply(claims);
    // }

    // private Claims extractAllClaims(String token) {
    // return Jwts.parserBuilder()
    // .setSigningKey(key)
    // .build()
    // .parseClaimsJws(token)
    // .getBody();
    // }

    // Phương thức này sẽ trích xuất thông tin userId từ JWT token. Nó sử dụng thư
    // viện JJWT để phân tích token và lấy ra thông tin subject (trong trường hợp
    // này là userId).
    public String getUserIdFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.getSubject();
    }

    public String getEmailFromJwt(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        return claims.get("email", String.class);
    }

    // 1. Token có đúng định dạng không (ví dụ: có bắt đầu bằng "Bearer " hay
    // không).
    // 2. Chữ ký của token có đúng không
    // 3. Kiểm tra token đã hết hạn chưa
    // 4. Kiểm tra email có khớp với userDetails hay không
    // 5. Kiểm tra token có trong blacklist hay không
    // 6. Kiểm tra quyền
    // public boolean isValidToken(String token, UserDetails userDetails) {
    // try {
    // // 1. Kiểm tra định dạng
    // if (!isTokenFormatValid(token)) {
    // logger.error("Invalid token format");
    // return false;
    // }

    // // 2. Kiểm tra chữ ký
    // if (!isTokenSignatureValid(token)) {
    // logger.error("Invalid token signature");
    // return false;
    // }

    // // 3. Kiểm tra hết hạn
    // if (isTokenExpired(token)) {
    // logger.error("Token has expired");
    // return false;
    // }

    // // 4. Kiểm tra nguồn gốc của token
    // if (!isIssuerToken(token)) {
    // logger.error("Invalid token issuer");
    // return false;
    // }

    // // 5. Kiểm tra email có khớp với userDetails hay không
    // final String emailFromToken = getEmailFromJwt(token);
    // if (!emailFromToken.equals(userDetails.getUsername())) {
    // logger.error("Invalid email in token");
    // return false;
    // }

    // // 6. Kiểm tra token có trong blacklist hay không

    // } catch (Exception e) {
    // // Log the exception or handle it as needed
    // logger.error("Invalid JWT token: " + e.getMessage());
    // return false;
    // }
    // return false;
    // }

    public boolean isTokenFormatValid(String token) {
        try {
            String[] parts = token.split("\\.");
            return parts.length == 3; // JWT token should have three parts separated by dots
        } catch (Exception e) {
            return false;
        }
    }

    public boolean isTokenSignatureValid(String token) {
        try {
            // logger.info(token);
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true; // If parsing is successful, the signature is valid
        } catch (ExpiredJwtException e) {
            logger.warn("Token expired while validating signature: {}", e.getMessage());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public Key getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecretKey().getBytes();
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(keyBytes));
    }

    public boolean isTokenExpired(String token) {
        try {
            Claims claims = getAllClaimsFromToken(token);

            if (claims != null) {
                return claims.getExpiration().before(new Date());
            } else {
                return true; // Token không hợp lệ, coi như hết hạn
            }

        } catch (Exception e) {
            return false; // Token không hợp lệ hoặc có lỗi khác, nhưng không phải là hết hạn
        }

    }

    public boolean isIssuerToken(String token) {
        String tokenIssuer = getClaimFromToken(token, Claims::getIssuer);
        return jwtConfig.getIssuer().equals(tokenIssuer);
    }

    public boolean isBlacklistedToken(String token) {
        return blacklistedTokenRepository.existsByToken(token);
    }

    public Claims getAllClaimsFromToken(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (ExpiredJwtException e) {
            logger.warn("Token has expired: " + e.getMessage());
            return e.getClaims(); // Trả về claims ngay cả khi token đã hết hạn
        } catch (Exception e) {
            logger.error("Error while parsing token: " + e.getMessage());
            return null;
        }

    }

    // public <T> giúp định nghĩa một phương thức tổng quát (generic method) trong
    // Java. Phương thức này có thể trả về bất kỳ kiểu dữ liệu nào được chỉ định bởi
    // T. Trong trường hợp này, phương thức getClaimFromToken nhận vào một token và
    // một Function<Claims, T> claimsResolver, cho phép bạn trích xuất thông tin cụ
    // thể từ Claims của JWT token.
    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public boolean isRefreshTokenValid(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            RefreshToken refreshToken = refreshTokenRepository.findByRefreshToken(token)
                    .orElseThrow(() -> new RuntimeException("Refresh token not found in database"));

            LocalDateTime expirationLocalDateTime = refreshToken.getExpiryDate();
            Date expirationDate = Date.from(expirationLocalDateTime.atZone(ZoneId.systemDefault()).toInstant());

            // final Date expiration = getClaimFromToken(refreshToken.getRefreshToken(),
            // Claims::getExpiration);
            return expirationDate.after(new Date());
        } catch (Exception e) {
            return false; // Refresh token is invalid
        }
    }

}
