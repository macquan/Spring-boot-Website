package PMQ.local.SpringBootProject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling // Annotation này cho phép Spring Boot tự động phát hiện và chạy các tác vụ được
					// đánh dấu bằng @Scheduled trong ứng dụng.
@EnableJpaRepositories(basePackages = "PMQ.local.SpringBootProject.modules") // Annotation này cho phép Spring Boot tự
																				// động phát hiện và quản lý các
																				// repository trong gói
																				// "PMQ.local.SpringBootProject.modules".
public class SpringBootProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootProjectApplication.class, args);
	}

}
