package PMQ.local.SpringBootProject.modules.users.services.interfaces;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.LoginRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.LoginResource;

public interface UserServiceInterface {
    LoginResource authenticate(LoginRequest request);
}
