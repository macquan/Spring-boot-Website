package PMQ.local.SpringBootProject.modules.users.services.interfaces;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.LoginRequest;

public interface UserServiceInterface {
    Object authenticate(LoginRequest request);
}
