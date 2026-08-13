package PMQ.local.SpringBootProject.modules.users.mappers;

import org.mapstruct.Mapper;

import PMQ.local.SpringBootProject.mappers.BaseMapper;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.Permission.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.Permission.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.PermissionResource;
import PMQ.local.SpringBootProject.modules.users.entities.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper
        extends BaseMapper<Permission, PermissionResource, StoreRequest, UpdateRequest> {

}
