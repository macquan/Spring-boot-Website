package PMQ.local.SpringBootProject.services;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import PMQ.local.SpringBootProject.config.JwtConfig;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    private final Key key;

    private static final Logger logger = LoggerFactory.getLogger(JwtService.class);

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
                .setSubject(userId.toString())
                .claim("email", email)
                .setIssuer(jwtConfig.getIssuer())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
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
            logger.info(token);
            Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token);
            return true; // If parsing is successful, the signature is valid
        } catch (SignatureException e) {
            return false;
        }
    }

    public Key getSigningKey() {
        byte[] keyBytes = jwtConfig.getSecretKey().getBytes();
        return Keys.hmacShaKeyFor(Base64.getEncoder().encode(keyBytes));
    }

    public boolean isTokenExpired(String token) {
        try {
            final Date expiration = getClaimFromToken(token, Claims::getExpiration);
            return expiration.after(new Date());

        } catch (ExpiredJwtException e) {
            return false; // Token đã hết hạn, nhưng chúng ta vẫn muốn xử lý nó trong JwtAuthFilter.java
        }

    }

    public boolean isIssuerToken(String token) {
        String tokenIssuer = getClaimFromToken(token, Claims::getIssuer);
        return jwtConfig.getIssuer().equals(tokenIssuer);
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
}
