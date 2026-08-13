package PMQ.local.SpringBootProject.controllers;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import PMQ.local.SpringBootProject.mappers.BaseMapper;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.UserCatalogueResource;
import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.BaseServiceInterface;
import PMQ.local.SpringBootProject.resources.APIResource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

public abstract class BaseController<E, R, C, U, Rp extends JpaRepository<E, Long> & JpaSpecificationExecutor<E>> {
    // E: Entity type
    // R: Resource type
    // C: Create request type
    // U: Update request type
    // Rp: Repository type

    protected final BaseServiceInterface<E, C, U> service;
    protected final BaseMapper<E, R, C, U> mapper;

    protected final Rp repo;

    public BaseController(BaseServiceInterface<E, C, U> service, BaseMapper<E, R, C, U> mapper, Rp repo) {
        this.service = service;
        this.mapper = mapper;
        this.repo = repo;
    }

    @GetMapping("/list")
    public ResponseEntity<?> list(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        List<E> entities = service.getAll(parameters);
        List<R> resources = mapper.toList(entities);
        APIResource<List<R>> response = APIResource.ok(resources,
                "Entities retrieved successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping
    // Get all user catalogues
    public ResponseEntity<?> pagination(HttpServletRequest request) {

        Map<String, String[]> parameters = request.getParameterMap();
        Page<E> entities = service.paginate(parameters);
        Page<R> resources = mapper.toResourcePage(entities);
        APIResource<Page<R>> response = APIResource.ok(resources,
                "Entities retrieved successfully");

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> store(@Valid @RequestBody C request) {

        try {
            E entity = service.create(request);
            R resource = mapper.toResource(entity);
            APIResource<R> response = APIResource.ok(resource,
                    "Entity created successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            String message = "An unexpected error occurred in the store operation: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResource.error("INTERNAL_SERVER_ERROR", message,
                            HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody U request) {

        try {
            E entity = service.update(id, request);
            R resource = mapper.toResource(entity);
            APIResource<R> response = APIResource.ok(resource,
                    "Entity updated successfully");

            return ResponseEntity.ok(response);

        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResource.error("NOT_FOUND", e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResource.error("INTERNAL_SERVER_ERROR",
                            "An unexpected error occurred in the update operation",
                            HttpStatus.INTERNAL_SERVER_ERROR));
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<?> show(@PathVariable Long id) {
        E entity = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Entity not found"));
        R resource = mapper.toResource(entity);
        APIResource<R> response = APIResource.ok(resource,
                "Entity retrieved successfully");

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            service.delete(id);
            return ResponseEntity.ok(APIResource.message("Deleted successfully", HttpStatus.OK));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResource.error("NOT_FOUND", e.getMessage(), HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResource.error("INTERNAL_SERVER_ERROR",
                            "An unexpected error occurred in the delete operation",
                            HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @DeleteMapping
    public ResponseEntity<?> deleteMany(@RequestBody List<Long> ids) {
        try {
            service.deleteMultipleEntity(ids);
            return ResponseEntity.ok(APIResource.message("Deleted successfully",
                    HttpStatus.OK));

        } catch (RuntimeException e) {
            String message = "An unexpected error occurred in the delete many operation: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(APIResource.error("NOT_FOUND", message, HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(APIResource.error("INTERNAL_SERVER_ERROR",
                            "An unexpected error occurred in the delete many operation: ",
                            HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

}
