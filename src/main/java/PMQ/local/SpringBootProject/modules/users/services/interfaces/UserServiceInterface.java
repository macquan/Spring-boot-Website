package PMQ.local.SpringBootProject.modules.users.services.interfaces;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.LoginRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.User.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.User.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.entities.User;

public interface UserServiceInterface extends BaseServiceInterface<User, StoreRequest, UpdateRequest> {
    Object authenticate(LoginRequest request);
}
