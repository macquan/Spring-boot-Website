package PMQ.local.SpringBootProject.modules.users.services.impls;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;
import PMQ.local.SpringBootProject.modules.users.mappers.UserCatalogueMapper;
import PMQ.local.SpringBootProject.modules.users.repositories.UserCatalogueRepository;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.UserCatalogueServiceInterface;
import PMQ.local.SpringBootProject.services.BaseService;

@Service // Annotation này đánh dấu lớp là một Service trong Spring, cho phép Spring tự
         // động phát hiện và quản lý nó như một bean trong container.
public class UserCatalogueService
        extends BaseService<UserCatalogue, UserCatalogueMapper, StoreRequest, UpdateRequest, UserCatalogueRepository>
        implements UserCatalogueServiceInterface {

    private static final Logger logger = LoggerFactory.getLogger(UserCatalogueService.class);

    @Autowired
    private UserCatalogueRepository userCatalogueRepository;

    private final UserCatalogueMapper userCatalogueMapper;

    public UserCatalogueService(UserCatalogueMapper userCatalogueMapper) {
        this.userCatalogueMapper = userCatalogueMapper;
    }

    @Override
    protected String[] getSearchFields() {
        return new String[] { "name" };
    }

    @Override
    protected String[] getRelations() {
        return new String[] { "permissions", "users" };
    }

    @Override
    protected UserCatalogueRepository getRepository() {
        return userCatalogueRepository;
    }

    @Override
    protected UserCatalogueMapper getMapper() {
        return userCatalogueMapper;
    }

}
