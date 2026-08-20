package PMQ.local.SpringBootProject.databases.seeder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import PMQ.local.SpringBootProject.modules.users.entities.User;
import PMQ.local.SpringBootProject.modules.users.repositories.UserRepository;

import org.springframework.security.crypto.password.PasswordEncoder;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Component
public class DatabaseSeeder implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    @Override
    public void run(String... args) throws Exception {
        // This method will be executed when the application starts
        // You can add your database seeding logic here
        if (isTableEmpty()) {
            // Seed the database with initial data
            String passwordEncode = passwordEncoder.encode("password");
            User user = User.builder()
                    .name("John Doe")
                    .email("john.doe@example.com")
                    .password(passwordEncode)
                    .phone("123-456-7890")
                    .address("Ecopark")
                    .build();
            userRepository.save(user);
            logger.info("Seeding user data.");
        }
    }

    private boolean isTableEmpty() {
        Long count = (Long) entityManager.createQuery("SELECT COUNT(id) FROM User").getSingleResult();
        return count == 0; // nếu bằng 0 là true còn ko bằng 0 là false
    }
}
