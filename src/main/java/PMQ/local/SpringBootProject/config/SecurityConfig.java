package PMQ.local.SpringBootProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import PMQ.local.SpringBootProject.helpers.JwtAuthFilter;
import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    // Add your security configuration here, such as authentication manager,
    // password encoder, etc.
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // đây là nơi bạn cấu hình các quy tắc bảo mật cho ứng dụng của mình. Ví dụ, bạn
        // có thể cấu hình các endpoint nào cần xác thực, các phương thức HTTP nào được
        // phép, và các chính sách bảo mật khác.
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // 1. Routes AUTH = no JWT
                        .requestMatchers("/v1/auth/login").permitAll() // Allow access to /v1/auth/login without
                                                                       // authentication
                        // 2. Routes PUBLIC
                        .requestMatchers("/v1/products").permitAll()
                        .anyRequest().authenticated() // Require authentication for all other requests
                )
                // Không lưu trữ session, mỗi request phải mang theo thông tin xác thực (ví dụ:
                // JWT token)
                .sessionManagement(session -> session.sessionCreationPolicy(
                        SessionCreationPolicy.STATELESS))
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Add the JWT
                                                                                             // authentication filter
                                                                                             // before the
                                                                                             // username/password
                                                                                             // authentication filter

        return http.build();
    }
}
