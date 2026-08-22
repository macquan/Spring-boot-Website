package PMQ.local.SpringBootProject.aspects;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import PMQ.local.SpringBootProject.annotations.RequirePermission;
import PMQ.local.SpringBootProject.controllers.BaseController;
import PMQ.local.SpringBootProject.helpers.CustomPermissionEvaluator;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.CustomUserDetail;
import jakarta.servlet.http.HttpServletRequest;

@Component
@Aspect // Annotation này đánh dấu lớp là một Aspect, cho phép nó chứa các advice (các
        // phương thức được thực thi tại các điểm cắt) và pointcut (các điểm cắt xác
        // định nơi advice sẽ được áp dụng).
public class PermissionAspect {
    // Dùng để kiểm tra quyền truy cập dựa trên annotation @RequirePermission

    private final Logger logger = LoggerFactory.getLogger(PermissionAspect.class);

    @Autowired
    private CustomPermissionEvaluator customPermissionEvaluator;

    @Before("@annotation(requirePermission)") // Mỗi khi phương thức này được gọi, trước khi thực hiện, sẽ kiểm tra
                                              // quyền truy cập của người dùng dựa trên annotation @RequirePermission
    public void checkPermission(JoinPoint joinPoint, RequirePermission requirePermission) {
        Object target = joinPoint.getTarget();
        if (target instanceof BaseController) {
            BaseController<?, ?, ?, ?, ?> controller = (BaseController<?, ?, ?, ?, ?>) target;
            String module = controller.getModule().getPrefix();
            String permission = module + ":" + requirePermission.action();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (!customPermissionEvaluator.hasPermission(authentication, permission)) {
                throw new AccessDeniedException("Unauthorized access");
            }

            if ("list".equals(requirePermission.action()) || "pagination".equals(requirePermission.action())) {
                handleListPermission(joinPoint, authentication, module, requirePermission.viewAll());
            }
        }
    }

    private void handleListPermission(JoinPoint joinPoint, Authentication authentication, String module,
            String viewAll) {
        Object[] arguments = joinPoint.getArgs();
        logger.info("arguments: {}", arguments);

        String permission = module + ":" + viewAll;
        Boolean checkViewAll = !viewAll.isEmpty()
                && customPermissionEvaluator.hasPermission(authentication, permission);

        if (!checkViewAll) {
            for (Object argument : arguments) {
                if (argument instanceof HttpServletRequest request) {
                    CustomUserDetail userDetails = (CustomUserDetail) authentication.getPrincipal();
                    Long userId = userDetails.getId();
                    request.setAttribute("userId", userId);
                }
            }
        }
    }
}
