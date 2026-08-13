package PMQ.local.SpringBootProject.modules.users.mappers;

import org.mapstruct.Mapper;

import PMQ.local.SpringBootProject.mappers.BaseMapper;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.UserCatalogueResource;
import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;

@Mapper(componentModel = "spring")
public interface UserCatalogueMapper
        extends BaseMapper<UserCatalogue, UserCatalogueResource, StoreRequest, UpdateRequest> {

    // UserCatalogueResource toResource(UserCatalogue entity);

    // List<UserCatalogueResource> toList(List<UserCatalogue> entities);

    // default Page<UserCatalogueResource> toResourcePage(Page<UserCatalogue> page)
    // {
    // return page.map(this::toResource);
    // }

}
