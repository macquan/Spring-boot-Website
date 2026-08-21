package PMQ.local.SpringBootProject.annotations;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import PMQ.local.SpringBootProject.validators.UniqueEmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ ElementType.FIELD }) // Chỉ định rằng annotation này có thể được áp dụng cho các trường (fields) của
                               // lớp.
@Retention(RetentionPolicy.RUNTIME) // Chỉ định rằng annotation này có thể được áp dụng cho các trường (fields) của
                                    // lớp.
@Documented // Dùng để chỉ ra rằng annotation này nên được bao gồm trong tài liệu Javadoc.
@Constraint(validatedBy = UniqueEmailValidator.class) // Chỉ định rằng annotation này sẽ được kiểm tra bởi lớp
                                                      // UniqueEmailValidator.
public @interface UniqueEmail {
    String message() default "Email already exists"; // Thông báo lỗi mặc định nếu email đã tồn tại.

    Class<?>[] groups() default {}; // Nhóm các ràng buộc, có thể được sử dụng để phân loại các ràng buộc.

    Class<? extends Payload>[] payload() default {}; // Dữ liệu bổ sung có thể được sử dụng
}
