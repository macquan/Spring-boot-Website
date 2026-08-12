package PMQ.local.SpringBootProject.modules.users.services.interfaces;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;

public interface BaseServiceInterface<E, C, U> {
    E create(C request);

    E update(Long id, U request);

    Boolean delete(Long id);

    Boolean deleteMultipleEntity(List<Long> ids);

    List<E> getAll(Map<String, String[]> parameters);

    Page<E> paginate(Map<String, String[]> parameters);
}
