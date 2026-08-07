package PMQ.local.SpringBootProject.modules.users.services.interfaces;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;

public interface UserCatalogueServiceInterface {

    UserCatalogue create(StoreRequest request);

    UserCatalogue update(Long id, UpdateRequest request);
}
