package PMQ.local.SpringBootProject.modules.users.mappers;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import PMQ.local.SpringBootProject.annotations.BaseMapperAnnotation;
import PMQ.local.SpringBootProject.mappers.BaseMapper;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.User.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.User.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.UserResource;
import PMQ.local.SpringBootProject.modules.users.entities.User;

@Mapper(componentModel = "spring")
public interface UserMapper extends BaseMapper<User, UserResource, StoreRequest, UpdateRequest> {

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "userCatalogues", ignore = true) // Bỏ qua trường permissions khi ánh xạ từ CreateRequest sang
                                                       // Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    User toEntity(StoreRequest createRequest);

    @Override
    @BaseMapperAnnotation
    @Mapping(target = "userCatalogues", ignore = true) // Bỏ qua trường permissions khi ánh xạ từ CreateRequest sang
                                                       // Entity
    @Mapping(target = "password", ignore = true) // Bỏ qua trường password khi ánh xạ từ UpdateRequest sang Entity
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateEntityFromRequest(UpdateRequest updateRequest, @MappingTarget User entity);
}
