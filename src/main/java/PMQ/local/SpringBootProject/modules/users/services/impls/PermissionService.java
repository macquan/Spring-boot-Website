package PMQ.local.SpringBootProject.modules.users.services.impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.Permission.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.Permission.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.entities.Permission;
import PMQ.local.SpringBootProject.modules.users.mappers.PermissionMapper;
import PMQ.local.SpringBootProject.modules.users.repositories.PermissionRepository;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.PermissionServiceInterface;
import PMQ.local.SpringBootProject.services.BaseService;

@Service // Annotation này đánh dấu lớp là một Service trong Spring, cho phép Spring tự
         // động phát hiện và quản lý nó như một bean trong container.
public class PermissionService
        extends BaseService<Permission, PermissionMapper, StoreRequest, UpdateRequest, PermissionRepository>
        implements PermissionServiceInterface {

    @Autowired
    private PermissionRepository PermissionRepository;

    private final PermissionMapper PermissionMapper;

    public PermissionService(PermissionMapper PermissionMapper) {
        this.PermissionMapper = PermissionMapper;
    }

    @Override
    protected String[] getSearchFields() {
        return new String[] { "name" };
    }

    @Override
    protected PermissionRepository getRepository() {
        return PermissionRepository;
    }

    @Override
    protected PermissionMapper getMapper() {
        return PermissionMapper;
    }

}
