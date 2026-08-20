package PMQ.local.SpringBootProject.modules.users.services.impls;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import PMQ.local.SpringBootProject.modules.users.dtos.resources.CustomUserDetail;
import PMQ.local.SpringBootProject.modules.users.entities.User;
import PMQ.local.SpringBootProject.modules.users.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CustomUserDetailsService implements UserDetailsService {

        private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);

        private final UserRepository userRepository;

        @Override
        @Transactional
        public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {

                User user = userRepository.findById(Long.valueOf(userId))
                                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

                List<GrantedAuthority> authorities = user.getUserCatalogues().stream()
                                .flatMap(catalogue -> catalogue.getPermissions().stream())
                                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                                .collect(Collectors.toList());

                // logger.info("Authorities: {}", authorities.size());

                return new CustomUserDetail(user.getId(), user.getEmail(), user.getPassword(), authorities);

                // return new
                // org.springframework.security.core.userdetails.User(user.getEmail(),
                // user.getPassword(),
                // authorities);
        }
}
