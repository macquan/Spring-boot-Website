package PMQ.local.SpringBootProject.resources;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Schema(description = "Standard API response structure")
@Data
@JsonInclude(JsonInclude.Include.NON_NULL) // Chỉ serialize các trường không null, giúp giảm kích thước dữ liệu trả về
                                           // và tránh gửi thông tin không cần thiết.
public class APIResource<T> { // <T> là một generic type parameter, cho phép bạn định nghĩa một lớp có thể làm
                              // việc với nhiều loại dữ liệu khác nhau mà không cần phải tạo ra nhiều lớp
                              // riêng biệt cho từng loại dữ liệu.
    @Schema(description = "Indicates whether the request was successful", example = "true")
    private boolean success;

    @Schema(description = "A message providing additional information about the response", example = "Operation completed successfully")
    private String message;

    @Schema(description = "The data returned by the API")
    @JsonProperty("data") // Đặt tên trường trong JSON là "data" thay vì tên biến trong Java, giúp giữ
                          // nguyên tên trường khi serialize sang JSON.
    private T data;

    @Schema(description = "HTTP status code of the response", example = "200")
    private HttpStatus status;

    @Schema(description = "Timestamp when the response was generated", example = "2023-10-10T10:10:10Z")
    private LocalDateTime timestamp;

    @Schema(description = "Error details if the request was not successful")
    private ErrorResource error;

    @Data
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorResource {
        private String code;
        private String message;
        private String detail;

        public ErrorResource(String message) {
            this.message = message;
        }

        public ErrorResource(String code, String message) {
            this.code = code;
            this.message = message;
        }

        public ErrorResource(String code, String message, String detail) {
            this.code = code;
            this.message = message;
            this.detail = detail;
        }
    }

    private APIResource() {
        this.timestamp = LocalDateTime.now();
    }

    public static <T> Builder<T> builder() {
        return new Builder<>();
    }

    public static class Builder<T> {
        private final APIResource<T> resource;

        private Builder() {
            resource = new APIResource<>();
        }

        public Builder<T> success(boolean success) {
            resource.success = success;
            return this;
        }

        public Builder<T> message(String message) {
            resource.message = message;
            return this;
        }

        public Builder<T> data(T data) {
            resource.data = data;
            return this;
        }

        public Builder<T> status(HttpStatus status) {
            resource.status = status;
            return this;
        }

        public Builder<T> error(ErrorResource error) {
            resource.error = error;
            return this;
        }

        public APIResource<T> build() {
            return resource;
        }
    }

    public static <T> APIResource<T> ok(T data, String message) {
        return APIResource.<T>builder()
                .success(true)
                .message(message)
                .data(data)
                .status(HttpStatus.OK)
                .build();
    }

    public static <T> APIResource<T> message(String message, HttpStatus status) {
        return APIResource.<T>builder()
                .success(true)
                .message(message)
                .status(status)
                .build();
    }

    public static <T> APIResource<T> error(String code, String message, HttpStatus status) {
        return APIResource.<T>builder()
                .success(false)
                .error(new ErrorResource(code, message))
                .status(status)
                .build();
    }
}

// Với resource có data
/*
 * {
 * "success": boolean,
 * "message": "string",
 * "data": {
 * "id": 1,
 * "name": ...
 * },
 * "status": String,
 * "timestamp": "2023-10-10T10:10:10Z"
 * }
 */

// Với resource chỉ có message
/*
 * {
 * "success",
 * "status",
 * "error": {
 * "code" ...,
 * "message"
 * },
 * "timestamp"
 * }
 */