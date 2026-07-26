package PMQ.local.SpringBootProject.modules.users.dtos.resources;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class TokenResource {
    private final String token;
}
