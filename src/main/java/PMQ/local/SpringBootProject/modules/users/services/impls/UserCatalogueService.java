package PMQ.local.SpringBootProject.modules.users.services.impls;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page; // Dùng để làm việc với các trang dữ liệu (pagination) trong Spring Data.
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.StoreRequest;
import PMQ.local.SpringBootProject.modules.users.dtos.requests.UserCatalogue.UpdateRequest;
import PMQ.local.SpringBootProject.modules.users.entities.UserCatalogue;
import PMQ.local.SpringBootProject.modules.users.repositories.UserCatalogueRepository;
import PMQ.local.SpringBootProject.modules.users.services.interfaces.UserCatalogueServiceInterface;
import PMQ.local.SpringBootProject.services.BaseService;
import jakarta.persistence.EntityNotFoundException;

@Service // Annotation này đánh dấu lớp là một Service trong Spring, cho phép Spring tự
         // động phát hiện và quản lý nó như một bean trong container.
public class UserCatalogueService extends BaseService implements UserCatalogueServiceInterface {

    @Autowired
    private UserCatalogueRepository userCatalogueRepository;

    @Override
    public Page<UserCatalogue> paginate(Map<String, String[]> parameters) {

        int page = parameters.containsKey("page") ? Integer.parseInt(parameters.get("page")[0]) : 1; // Lấy số trang từ
                                                                                                     // tham số "page",
                                                                                                     // mặc định là 1
                                                                                                     // nếu không có.

        // Lấy số lượng bản ghi trên mỗi trang từ tham số "per_page", mặc định là 20 nếu
        // không có.
        int perPage = parameters.containsKey("per_page") ? Integer.parseInt(parameters.get("per_page")[0]) : 20;

        String sortParam = parameters.containsKey("sort") ? parameters.get("sort")[0] : null; // Lấy tham số "sort" từ
                                                                                              // parameters, mặc định là
                                                                                              // null nếu không có.

        Sort sort = createSort(sortParam); // Gọi phương thức createSort để tạo đối tượng Sort dựa trên tham số
                                           // sortParam.

        Pageable pageable = PageRequest.of(page - 1, perPage, sort); // Tạo đối tượng Pageable với số trang, số lượng
                                                                     // bản
        // ghi trên mỗi trang và đối tượng Sort.

        return userCatalogueRepository.findAll(pageable);
    }

    @Override
    @Transactional // Annotation này đánh dấu phương thức là một giao dịch (transactional). Nó đảm
                   // bảo rằng tất cả các thao tác trong phương thức sẽ được thực hiện trong một
                   // giao dịch duy nhất. Nếu có bất kỳ lỗi nào xảy ra trong quá trình thực hiện,
                   // tất cả các thay đổi sẽ được rollback để đảm bảo tính toàn vẹn dữ liệu.
    public UserCatalogue create(StoreRequest request) {

        try {
            UserCatalogue payload = UserCatalogue.builder()
                    .name(request.getName())
                    .publish(request.getPublish())
                    .build();

            return userCatalogueRepository.save(payload);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create user catalogue: " + e.getMessage());
        }

    }

    @Override
    @Transactional
    public UserCatalogue update(Long id, UpdateRequest request) {

        UserCatalogue userCatalogue = userCatalogueRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User catalogue not found with id: " + id));

        UserCatalogue payload = userCatalogue.toBuilder()
                .name(request.getName())
                .publish(request.getPublish())
                .build();

        return userCatalogueRepository.save(payload);
    }

}
