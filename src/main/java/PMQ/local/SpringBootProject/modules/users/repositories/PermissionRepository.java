package PMQ.local.SpringBootProject.modules.users.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import PMQ.local.SpringBootProject.modules.users.entities.Permission;

@Repository
public interface PermissionRepository
        extends JpaRepository<Permission, Long>, JpaSpecificationExecutor<Permission> {
    // Optional<Permission> findById(Long id);
}
