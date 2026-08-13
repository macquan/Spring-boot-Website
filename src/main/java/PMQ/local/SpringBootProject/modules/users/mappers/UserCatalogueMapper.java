package PMQ.local.SpringBootProject.modules.users.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import PMQ.local.SpringBootProject.annotations.BaseMapperAnnotation;
import PMQ.local.SpringBootProject.mappers.BaseMapper;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.UserCatalogueResource;
import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;

@Mapper(componentModel = "spring")
public interface UserCatalogueMapper
        extends BaseMapper<UserCatalogue, UserCatalogueResource, StoreRequest, UpdateRequest> {

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "permissions", ignore = true) // Bỏ qua trường permissions khi ánh xạ từ CreateRequest sang Entity
    @Mapping(target = "users", ignore = true) // Bỏ qua trường permissions khi ánh xạ từ CreateRequest sang Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    UserCatalogue toEntity(StoreRequest createRequest);

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "permissions", ignore = true) // Bỏ qua trường permissions khi ánh xạ từ UpdateRequest sang Entity
    @Mapping(target = "users", ignore = true) // Bỏ qua trường permissions khi ánh xạ từ CreateRequest sang Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateRequest updateRequest, @MappingTarget UserCatalogue entity);
}
