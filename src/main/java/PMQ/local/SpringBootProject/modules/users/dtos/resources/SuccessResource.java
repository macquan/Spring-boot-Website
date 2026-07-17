package PMQ.local.SpringBootProject.modules.users.dtos.resources;

public class SuccessResource<T> { // <T> là một generic type parameter, cho phép bạn định nghĩa một lớp có thể làm
                                  // việc với nhiều loại dữ liệu khác nhau mà không cần phải tạo ra nhiều lớp
                                  // riêng biệt cho từng loại dữ liệu.

    private String message;
    private T data;

    public SuccessResource(String message, T data) {
        this.message = message;
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
