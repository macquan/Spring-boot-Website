package PMQ.local.SpringBootProject.cronjob;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import PMQ.local.SpringBootProject.modules.users.repositories.BlacklistedTokenRepository;

@Service // Giúp Spring Boot tự động phát hiện và quản lý bean của lớp này, kiểm tra các
         // điều kiện logic của service trước khi gọi tầng @Repository để tương tác với
         // database
public class BlacklistTokenClean {
    @Autowired
    private BlacklistedTokenRepository blacklistedTokenRepository;

    private static final Logger logger = LoggerFactory.getLogger(BlacklistTokenClean.class);

    @Transactional // Đảm bảo rằng các thao tác xóa dữ liệu khỏi cơ sở dữ liệu được thực hiện trong
                   // một giao dịch, giúp đảm bảo tính toàn vẹn dữ liệu và tránh các vấn đề liên
                   // quan đến việc xóa dữ liệu không thành công
    @Scheduled(cron = "0 0 0 * * ?") // Chạy vào lúc 00:00 hàng ngày
    public void cleanupExpiredTokens() {
        LocalDateTime currentDateTime = LocalDateTime.now();
        int deletedCount = blacklistedTokenRepository.deleteByExpiryDateBefore(currentDateTime);
        logger.info("Deleted {} expired tokens from the blacklist.", deletedCount);
    }
}
