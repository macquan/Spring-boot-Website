package PMQ.local.SpringBootProject.modules.users.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import PMQ.local.SpringBootProject.annotations.BaseMapperAnnotation;
import PMQ.local.SpringBootProject.mappers.BaseMapper;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.Permission.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.Permission.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.PermissionResource;
import PMQ.local.SpringBootProject.modules.users.entities.Permission;

@Mapper(componentModel = "spring")
public interface PermissionMapper
                extends BaseMapper<Permission, PermissionResource, StoreRequest, UpdateRequest> {
        @Override
        @BaseMapperAnnotation
        @Mapping(target = "userCatalogues", ignore = true) // Bỏ qua trường permissions khi ánh xạ từ CreateRequest sang
                                                           // Entity
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        Permission toEntity(StoreRequest createRequest);

        @Override
        @BaseMapperAnnotation
        @Mapping(target = "userCatalogues", ignore = true) // Bỏ qua trường permissions khi ánh xạ từ CreateRequest sang
                                                           // Entity
        @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
        void updateEntityFromRequest(UpdateRequest updateRequest, @MappingTarget Permission entity);
}
