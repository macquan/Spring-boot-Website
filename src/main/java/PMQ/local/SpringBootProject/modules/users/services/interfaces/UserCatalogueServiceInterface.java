package PMQ.local.SpringBootProject.modules.users.services.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;

public interface UserCatalogueServiceInterface {

    UserCatalogue create(StoreRequest request);

    UserCatalogue update(Long id, UpdateRequest request);

    List<UserCatalogue> getAll(Map<String, String[]> parameters);

    Page<UserCatalogue> paginate(Map<String, String[]> parameters);

}
