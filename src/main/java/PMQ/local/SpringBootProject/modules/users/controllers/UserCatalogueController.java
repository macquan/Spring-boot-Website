package PMQ.local.SpringBootProject.modules.users.controllers;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PMQ.local.SpringBootProject.controllers.BaseController;
import PMQ.local.SpringBootProject.enums.PermissionEnum;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.UserCatalogueResource;
import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;
import PMQ.local.SpringBootProject.modules.users.mappers.UserCatalogueMapper;
import PMQ.local.SpringBootProject.modules.users.repositories.UserCatalogueRepository;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.UserCatalogueServiceInterface;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "User Catalogue API", description = "API for managing user catalogues")
@Validated // Dùng để kích hoạt việc kiểm tra các ràng buộc (constraints) trên các tham số
           // của phương thức trong controller.
@RestController
@RequestMapping("/api/v1/user_catalogues")
public class UserCatalogueController extends
                BaseController<UserCatalogue, UserCatalogueResource, StoreRequest, UpdateRequest, UserCatalogueRepository> {

        public UserCatalogueController(UserCatalogueServiceInterface service,
                        UserCatalogueMapper mapper, UserCatalogueRepository repo) {

                super(service, mapper, repo, PermissionEnum.USER_CATALOGUE);
        }

}
