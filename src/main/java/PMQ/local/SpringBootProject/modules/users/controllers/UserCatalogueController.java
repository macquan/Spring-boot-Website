package PMQ.local.SpringBootProject.modules.users.controllers;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.resources.UserCatalogueResource;
import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.UserCatalogueServiceInterface;
import PMQ.local.SpringBootProject.resources.APIResource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest; // Dùng để làm việc với các yêu cầu HTTP trong Spring Boot.
import jakarta.validation.Valid;

@Validated // Dùng để kích hoạt việc kiểm tra các ràng buộc (constraints) trên các tham số
           // của phương thức trong controller.
@RestController
@RequestMapping("/api/v1")
public class UserCatalogueController {

        private static final Logger logger = LoggerFactory.getLogger(UserCatalogueController.class);

        private final UserCatalogueServiceInterface userCatalogueService;

        public UserCatalogueController(UserCatalogueServiceInterface userCatalogueService) {
                this.userCatalogueService = userCatalogueService;
        }

        @GetMapping("/user_catalogues/all")
        public ResponseEntity<?> list(HttpServletRequest request) {
                Map<String, String[]> parameters = request.getParameterMap();

                List<UserCatalogue> userCatalogues = userCatalogueService.getAll(parameters);

                List<UserCatalogueResource> userCatalogueResources = userCatalogues.stream()
                                .map(userCatalogue -> UserCatalogueResource.builder()
                                                .id(userCatalogue.getId())
                                                .name(userCatalogue.getName())
                                                .publish(userCatalogue.getPublish())
                                                .build())
                                .collect(Collectors.toList());

                APIResource<List<UserCatalogueResource>> response = APIResource.ok(userCatalogueResources,
                                "User catalogues retrieved successfully");

                return ResponseEntity.ok(response);
        }

        @GetMapping("/user_catalogues")
        // Get all user catalogues
        public ResponseEntity<?> index(HttpServletRequest request) {

                Map<String, String[]> parameters = request.getParameterMap();

                Page<UserCatalogue> userCatalogues = userCatalogueService.paginate(parameters);

                Page<UserCatalogueResource> userCatalogueResources = userCatalogues
                                .map(userCatalogue -> UserCatalogueResource.builder()
                                                .id(userCatalogue.getId())
                                                .name(userCatalogue.getName())
                                                .publish(userCatalogue.getPublish())
                                                .build());

                APIResource<Page<UserCatalogueResource>> response = APIResource.ok(userCatalogueResources,
                                "User catalogues retrieved successfully");

                return ResponseEntity.ok(response);
        }

        @PostMapping("user_catalogues")
        public ResponseEntity<?> store(@Valid @RequestBody StoreRequest request) {

                UserCatalogue userCatalogue = userCatalogueService.create(request);

                UserCatalogueResource userCatalogueResource = UserCatalogueResource.builder()
                                .id(userCatalogue.getId())
                                .name(userCatalogue.getName())
                                .publish(userCatalogue.getPublish())
                                .build();

                APIResource<UserCatalogueResource> response = APIResource.ok(userCatalogueResource,
                                "User catalogue created successfully");

                logger.info("Method Running...!");
                return ResponseEntity.ok(response);
        }

        @PutMapping("user_catalogues/{id}")
        public ResponseEntity<?> update(@PathVariable Long id, @Valid @RequestBody UpdateRequest request) {

                try {

                        UserCatalogue userCatalogue = userCatalogueService.update(id, request);
                        UserCatalogueResource userCatalogueResource = UserCatalogueResource.builder()
                                        .id(userCatalogue.getId())
                                        .name(userCatalogue.getName())
                                        .publish(userCatalogue.getPublish())
                                        .build();

                        APIResource<UserCatalogueResource> response = APIResource.ok(userCatalogueResource,
                                        "User catalogue updated successfully");

                        logger.info("Method Running...!");
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
}
