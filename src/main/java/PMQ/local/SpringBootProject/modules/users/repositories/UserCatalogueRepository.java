package PMQ.local.SpringBootProject.modules.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;

@Repository
public interface UserCatalogueRepository
        extends JpaRepository<UserCatalogue, Long>, JpaSpecificationExecutor<UserCatalogue> {
    // Optional<UserCatalogue> findById(Long id);
}
