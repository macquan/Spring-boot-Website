// package PMQ.local.SpringBootProject;

// import org.springframework.jdbc.core.JdbcTemplate;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestMapping;
// import org.springframework.web.bind.annotation.RestController;

// @RestController
// @RequestMapping("v1/api/")
// public class BaseController {

//     private final JdbcTemplate jdbcTemplate;

//     public BaseController(JdbcTemplate jdbcTemplate) {
//         this.jdbcTemplate = jdbcTemplate;
//     }

//     @GetMapping("test")
//     public String test() {
        
//         String sql = "Create table if not exists test_table (id int primary key auto_increment, name varchar(255) not null, email varchar(255))";
//         jdbcTemplate.execute(sql);

//         return "Đã tạo bảng thành công!";
//      }
// }
