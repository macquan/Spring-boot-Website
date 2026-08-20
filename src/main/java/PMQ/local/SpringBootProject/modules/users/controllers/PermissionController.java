package PMQ.local.SpringBootProject.modules.users.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PMQ.local.SpringBootProject.controllers.BaseController;
import PMQ.local.SpringBootProject.enums.PermissionEnum;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.Permission.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.Permission.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.PermissionResource;
import PMQ.local.SpringBootProject.modules.users.entities.Permission;
import PMQ.local.SpringBootProject.modules.users.mappers.PermissionMapper;
import PMQ.local.SpringBootProject.modules.users.repositories.PermissionRepository;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.PermissionServiceInterface;

// Dùng để kích hoạt việc kiểm tra các ràng buộc (constraints) trên các tham số của phương thức trong controller.
@Validated
@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController
        extends BaseController<Permission, PermissionResource, StoreRequest, UpdateRequest, PermissionRepository> {

    public PermissionController(PermissionServiceInterface service,
            PermissionMapper mapper, PermissionRepository repo) {
        super(service, mapper, repo, PermissionEnum.PERMISSION);
    }

}
