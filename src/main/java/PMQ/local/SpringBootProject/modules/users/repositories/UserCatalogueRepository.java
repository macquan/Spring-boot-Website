package PMQ.local.SpringBootProject.modules.users.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;

@Repository
public interface UserCatalogueRepository extends JpaRepository<UserCatalogue, Long> {
    // Optional<UserCatalogue> findById(Long id);
}
