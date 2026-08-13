package PMQ.local.SpringBootProject.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.mapstruct.Mapping;

@Retention(RetentionPolicy.CLASS) // Annotation này được giữ lại trong bytecode nhưng không có sẵn tại runtime. Nó
                                  // được sử dụng để cung cấp thông tin cho các công cụ xử lý annotation trong quá
                                  // trình biên dịch.
@Target(ElementType.METHOD) // Annotation này có thể được áp dụng cho các lớp, giao diện (interface), hoặc
                            // enum.
@Mapping(target = "id", ignore = true) // Bỏ qua trường id khi ánh xạ từ CreateRequest sang Entity
@Mapping(target = "createdAt", ignore = true) // Bỏ qua trường createdAt khi ánh xạ từ CreateRequest sang Entity
@Mapping(target = "updatedAt", ignore = true) // Bỏ qua trường updated

public @interface BaseMapperAnnotation {

}
